package net.esportsbets.service;

import net.esportsbets.dao.*;
import net.esportsbets.model.BetsRequestModel;
import net.esportsbets.model.UserBetRequestModel;
import net.esportsbets.model.UserBetsResponse;
import net.esportsbets.repository.*;
import net.esportsbets.repository.hibernate.BetsHibernateRepository;
import net.esportsbets.repository.hibernate.MatchHibernateRepository;
import net.esportsbets.service.helper.BetsServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BetsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBetsRepository userBetsRepository;

    @Autowired
    private UserCreditsRepository userCreditsRepository;

    @Autowired
    private BetsHibernateRepository betsHibernateRepository;

    @Autowired
    private MatchInfoService matchInfoService;

    @Autowired
    private MatchHibernateRepository matchRepository;

    @Autowired
    private UserBetsDetailsLockRepository userBetsDetailsLockRepository;

    @Autowired
    private BetsServiceHelper betsServiceHelper;



    public Double getUserCredits(String userEmail) {
        User user = userRepository.findByEmail( userEmail );
        UserCredits userCredits = userCreditsRepository.findById(user.getId()).get();
        return userCredits.getCredits();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> placeBets(UserBetRequestModel betRequest, String userEmail) {

        if ( betRequest.getBets()==null || betRequest.getBets().isEmpty() ) {
            return ResponseEntity.badRequest().body( "No bets selected" );
        }

        if ( betRequest.getAmount() == null || betRequest.getAmount()<0.0 ) {
            return ResponseEntity.badRequest().body( "invalid bet amount" );
        }

        if ( betRequest.getBetType().equals("SINGLE") &&
                betRequest.getBets().size() > 1 ) {
            return ResponseEntity.badRequest().body( "Multiple bets selected for Singular bet" );
        }

        User user = userRepository.findByEmail( userEmail );

        UserCredits userCredits = userCreditsRepository.findById(user.getId()).get();
        if( userCredits.getCredits() < betRequest.getAmount() ) {
            return ResponseEntity.badRequest().body( "Insufficient balance for this bet" );
        }

        userCredits = betsServiceHelper.debitUser( user, betRequest.getAmount(), null, "holding bet amount" );

        try {
            List<String> matchIds = betRequest.getBets()
                                                .stream()
                                                .map(BetsRequestModel::getMatchId)
                                                .collect(Collectors.toList());

            Set<Matches> matches  = matchRepository.getMatchesAfterTime( matchIds,
                                                        new Timestamp( new java.util.Date().getTime() ) );

            Set<String> uniqueMatchIds = new HashSet<>(matchIds);

            if ( uniqueMatchIds.size() != matches.size() ) {
                return ResponseEntity.badRequest().body( "Some of the matches in the bet have already started" );
            }

            List<UserBetsDetailsLock> userBetsDetailsList = userBetsDetailsLockRepository.findByUserIdEqualsAndMatchIdIn(
                                                                                                user.getId(), matchIds );

            UserBets userBet = new UserBets();
            userBet.setBetsComposition( UserBets.UserBetsComposition.valueOf( betRequest.getBetType() ) );
            userBet.setUserId( user.getId() );
            userBet.setAmount( betRequest.getAmount() );
            userBet.setConcluded( UserBets.Conclusion.IN_PROGRESS );
            userBet.setUserBets( new HashSet<>());
            userBet.setOdds( 1.0 );
            betRequest.getBets().forEach( singleBet -> {
                Bets bet = new Bets();
                UserBetsDetailsLock userBetsDetailsLock =  userBetsDetailsList.stream()
                        .filter(betDetail ->
                                betDetail.getMatchId().equals(singleBet.getMatchId()))
                        .findFirst()
                        .get();
                bet.setMatchId( singleBet.getMatchId() );
                bet.setBetType( Bets.BetType.valueOf( singleBet.getBetType() ) );
                bet.setTeamId( singleBet.getTeamId() );
                bet.setConcluded( Bets.Conclusion.IN_PROGRESS );
                bet.setSpread( singleBet.getTeamId().equals(0) ? userBetsDetailsLock.getTeam0Spread() : userBetsDetailsLock.getTeam1Spread() );
                if ( bet.getBetType() == Bets.BetType.MONEYLINE ) {
                    userBet.setOdds( userBet.getOdds() *
                            ( singleBet.getTeamId().equals(0) ? userBetsDetailsLock.getTeam0MoneylineOdds() : userBetsDetailsLock.getTeam1MoneylineOdds() ) );
                } else if ( bet.getBetType() == Bets.BetType.SPREAD ) {
                    userBet.setOdds( userBet.getOdds() *
                            ( singleBet.getTeamId().equals(0) ? userBetsDetailsLock.getTeam0SpreadOdds() : userBetsDetailsLock.getTeam1SpreadOdds() ) );
                }
                userBet.getUserBets().add(bet);
            } );

            UserBets savedUserBet = userBetsRepository.save( userBet );

            userCredits = betsServiceHelper.creditUser( user, betRequest.getAmount(), null, "releasing the bet hold" );
            userCredits = betsServiceHelper.debitUser( user, betRequest.getAmount(), savedUserBet.getId(), "placing bet" );

        } catch ( Exception ex ) {
            userCredits = betsServiceHelper.creditUser( user, betRequest.getAmount(), null, "releasing the bet hold" );
        }
        return ResponseEntity.accepted().body( userCredits.getCredits().toString() );
    }


    @Transactional(rollbackFor = Exception.class)
    public void createUserCredit( User user, Double amount, Long betId, String comment ) {
        UserCredits userCredits = new UserCredits();
        userCredits.setCredits( 0.0 );
        userCredits.setUserId( user.getId() );
        userCreditsRepository.save(userCredits);
        betsServiceHelper.creditUser( user, amount, betId, comment );
    }

    public List<UserBetsResponse> getBetsForUser(String userEmail ) {
        User user = userRepository.findByEmail( userEmail );
        Set<UserBets> userBets = betsHibernateRepository.getBetsForUser( user.getId() );

        List<Matches> matches = userBets.parallelStream()
                                        .map(UserBets::getUserBets)
                                        .flatMap(Collection::stream)
                                        .map(Bets::getMatch)
                                        .collect(Collectors.toList());

        matchInfoService.updateSpreadForMatches( matches );

        return userBets.stream()
                .sorted(Comparator.comparing( (UserBets userBet) -> userBet.getConcluded()== UserBets.Conclusion.IN_PROGRESS)
                                    .thenComparing(userBet -> userBet.getTime()).reversed())
                        .map(userBet -> UserBetsResponse.getInstance(userBet, betsServiceHelper))
                        .collect(Collectors.toList());
    }
}
