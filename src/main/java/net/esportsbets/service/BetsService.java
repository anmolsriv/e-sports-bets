package net.esportsbets.service;

import net.esportsbets.dao.*;
import net.esportsbets.model.UserBetRequestModel;
import net.esportsbets.model.UserBetsResponse;
import net.esportsbets.repository.UserBetsRepository;
import net.esportsbets.repository.UserCreditsRepository;
import net.esportsbets.repository.UserRepository;
import net.esportsbets.repository.UserTransactionHistoryRepository;
import net.esportsbets.repository.hibernate.BetsHibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserTransactionHistoryRepository userTransactionHistoryRepository;

    @Autowired
    private BetsHibernateRepository betsHibernateRepository;

    @Autowired
    private MatchInfoService matchInfoService;

    public Double getUserCredits(String userEmail) {
        User user = userRepository.findByEmail( userEmail );
        UserCredits userCredits = userCreditsRepository.findById(user.getId()).get();
        return userCredits.getCredits();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public Double placeBets(UserBetRequestModel betRequest, String userEmail) {

        if ( betRequest.getBetType().equals("SINGLE") &&
                betRequest.getBets().size() > 1 ) {
            return -1.0;
        }

        User user = userRepository.findByEmail( userEmail );

        UserCredits userCredits = userCreditsRepository.findById(user.getId()).get();
        if( userCredits.getCredits()<betRequest.getAmount() ) {
            return -1.0;
        }
        UserBets userBet = new UserBets();
        userBet.setBetsComposition( UserBets.UserBetsComposition.valueOf( betRequest.getBetType() ) );
        userBet.setUserId( user.getId() );
        userBet.setAmount( betRequest.getAmount() );
        userBet.setOdds( betRequest.getOdds() );
        userBet.setConcluded( UserBets.Conclusion.IN_PROGRESS );
        userBet.setUserBets( new HashSet<>());
        betRequest.getBets().forEach( singleBet -> {
            Bets bet = new Bets();
            bet.setMatchId( singleBet.getMatchId() );
            bet.setBetType( Bets.BetType.valueOf( singleBet.getBetType() ) );
            bet.setTeamId( singleBet.getTeamId() );
            bet.setConcluded( Bets.Conclusion.IN_PROGRESS );
            bet.setSpread( singleBet.getSpread() );
            userBet.getUserBets().add(bet);
        } );
        UserBets savedUserBet = userBetsRepository.save( userBet );

        userCredits = debitUser( user, betRequest.getAmount(), savedUserBet.getId(), "placing bet" );
        return userCredits.getCredits();
    }


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public void createUserCredit( User user, Double amount, Long betId, String comment ) {
        UserCredits userCredits = new UserCredits();
        userCredits.setCredits( 0.0 );
        userCredits.setUserId( user.getId() );
        userCreditsRepository.save(userCredits);
        creditUser( user, amount, betId, comment );
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    @Async
    public UserCredits creditUser( User user, Double amount, Long betId, String comment ) {
        UserCredits userCredits = userCreditsRepository.findById( user.getId() ).get();
        userCredits.setCredits( userCredits.getCredits() + amount );
        UserTransactionHistory log = new UserTransactionHistory();
        log.setAmount( amount );
        log.setBetId( betId );
        log.setTransactionType( UserTransactionHistory.TransactionType.DEBIT );
        log.setUserCreditId( userCredits.getUserId() );
        log.setComment( comment );
        userTransactionHistoryRepository.save(log);
        return userCreditsRepository.save( userCredits );
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    @Async
    public UserCredits debitUser( User user, Double amount, Long betId, String comment ) {
        UserCredits userCredits = userCreditsRepository.findById( user.getId() ).get();
        userCredits.setCredits( userCredits.getCredits() - amount );
        UserTransactionHistory log = new UserTransactionHistory();
        log.setAmount( amount );
        log.setBetId( betId );
        log.setTransactionType( UserTransactionHistory.TransactionType.CREDIT );
        log.setUserCreditId( userCredits.getUserId() );
        log.setComment( comment );
        userTransactionHistoryRepository.save(log);
        return userCreditsRepository.save( userCredits );
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
                        .map(UserBetsResponse::getInstance)
                        .collect(Collectors.toList());
    }
}
