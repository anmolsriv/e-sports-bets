package net.esportsbets.service.helper;

import net.esportsbets.dao.*;
import net.esportsbets.repository.UserCreditsRepository;
import net.esportsbets.repository.UserTransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BetsServiceHelper {

    @Autowired
    private UserCreditsRepository userCreditsRepository;

    @Autowired
    private UserTransactionHistoryRepository userTransactionHistoryRepository;



    @Transactional(rollbackFor = Exception.class)
    public UserCredits creditUser(User user, Double amount, Long betId, String comment ) {
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

    @Transactional(rollbackFor = Exception.class)
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

    public Bets.Conclusion processSpreadBet(MatchScores[] matchScores, Integer teamId, Double spread ) {

        // calculating score(team user bet on) - score(other team)
        int scoresDiff = matchScores[0].getScore() - matchScores[1].getScore();
        if ( matchScores[1].getTeamId().equals( teamId ) ) {
            scoresDiff *= -1;
        }

        if ( -1*spread == scoresDiff ) {
            return Bets.Conclusion.PUSH;
        } else if ( scoresDiff > -1*spread ) {
            return Bets.Conclusion.WIN;
        }

        return Bets.Conclusion.LOSS;
    }
}
