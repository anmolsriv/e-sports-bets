package net.esportsbets.service;

import net.esportsbets.dao.*;
import net.esportsbets.model.UserBetRequestModel;
import net.esportsbets.repository.UserBetsRepository;
import net.esportsbets.repository.UserCreditsRepository;
import net.esportsbets.repository.UserRepository;
import net.esportsbets.repository.UserTransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashSet;

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

    public Double getUserCredits(String userEmail) {
        User user = userRepository.findByEmail( userEmail );
        UserCredits userCredits = userCreditsRepository.findById(user.getId()).get();
        return userCredits.getCredits();
    }

    @Transactional(rollbackFor = Exception.class)
    public Double placeBets(UserBetRequestModel betRequest, String userEmail) {

        User user = userRepository.findByEmail( userEmail );

        UserCredits userCredits = userCreditsRepository.findById(user.getId()).get();
        if(userCredits.getCredits()<betRequest.getAmount()) {
            return -1.0;
        }
        UserBets userBet = new UserBets();
        userBet.setBetsComposition(UserBets.UserBetsComposition.valueOf(betRequest.getBetType()) );
        userBet.setUserId( user.getId() );
        userBet.setAmount( betRequest.getAmount() );
        userBet.setOdds( betRequest.getOdds() );
        userBet.setConcluded( UserBets.Conclusion.IN_PROGRESS );
        userBet.setUserBets( new HashSet<>());
        betRequest.getBets().forEach( singleBet -> {
            Bets bet = new Bets();
            bet.setMatchId( singleBet.getMatchId() );
            bet.setBetType( Bets.BetType.valueOf(singleBet.getBetType()) );
            bet.setTeamId( singleBet.getTeamId() );
            bet.setConcluded( Bets.Conclusion.IN_PROGRESS );
            userBet.getUserBets().add(bet);
        } );
        UserBets savedUserBet = userBetsRepository.save(userBet);
        userCredits.setCredits(userCredits.getCredits()-betRequest.getAmount());
        userCreditsRepository.save(userCredits);
        UserTransactionHistory log = new UserTransactionHistory();
        log.setAmount( betRequest.getAmount() );
        log.setTransactionType(UserTransactionHistory.TransactionType.DEBIT);
        log.setUserCreditId( userCredits.getUserId() );
        log.setBetId( savedUserBet.getId() );
        userTransactionHistoryRepository.save(log);
        return 0.0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createUserCredit( User user ) {
        UserCredits userCredits = new UserCredits();
        userCredits.setCredits( 2000.0 );
        userCredits.setUserId( user.getId() );
        UserCredits savedUserCredits = userCreditsRepository.save(userCredits);
        UserTransactionHistory log = new UserTransactionHistory();
        log.setAmount( 2000.0 );
        log.setTransactionType(UserTransactionHistory.TransactionType.CREDIT);
        log.setUserCreditId( savedUserCredits.getUserId() );
        log.setComment( "new user bonus credit" );
        userTransactionHistoryRepository.save(log);
    }
}
