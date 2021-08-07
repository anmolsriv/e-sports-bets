package net.esportsbets.service.helper;

import net.esportsbets.dao.User;
import net.esportsbets.dao.UserCredits;
import net.esportsbets.dao.UserTransactionHistory;
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



    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    @Async
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
}
