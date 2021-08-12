package net.esportsbets.schedular;

import net.esportsbets.dao.*;
import net.esportsbets.repository.UserBetsRepository;
import net.esportsbets.repository.UserRepository;
import net.esportsbets.repository.hibernate.BetsHibernateRepository;
import net.esportsbets.service.BetsService;
import net.esportsbets.service.helper.BetsServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Set;

@Service
public class BetsStatusUpdateSchedular {

    @Autowired
    private BetsHibernateRepository betsHibernateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BetsServiceHelper betsServiceHelper;

    @Scheduled(cron = "0 1/2 * 1/1 * ?")
    public void computeBetResults() {

        Timestamp currTime = new Timestamp( new java.util.Date().getTime() );
        Set<UserBets> pendingBets = betsHibernateRepository.customMatchSearchQuery( currTime ) ;
        if (pendingBets.size()==0) {
            return;
        }
        pendingBets.forEach(pendingBet -> processBetResults(pendingBet, currTime) );
        betsHibernateRepository.updateUserBetsConclusion( pendingBets );
    }

    private void processBetResults( UserBets bet, Timestamp currTime ) {

        User user = userRepository.findById( bet.getUserId() ).get();
        for (Bets subBet : bet.getUserBets()) {
            if ( subBet.getConcluded() == Bets.Conclusion.IN_PROGRESS &&
                    subBet.getMatch().getTime().before( currTime ) ) {
                subBet.setConcluded( Bets.Conclusion.LOSS );
                if ( subBet.getBetType() == Bets.BetType.SPREAD ) {
                    MatchScores[] matchScores = subBet.getMatch().getMatchScores().toArray(new MatchScores[0]);
                    subBet.setConcluded( betsServiceHelper.processSpreadBet( matchScores, subBet.getTeamId(), subBet.getSpread() ) );
                } else if ( subBet.getMatch().getWinner().equals( subBet.getTeamId() ) ) {
                    subBet.setConcluded( Bets.Conclusion.WIN );
                }
            }
        }
        boolean allBetsConcluded = true;
        boolean allBetsWin = true;
        boolean pushConclusion = false;
        for (Bets subBet : bet.getUserBets()) {
            if ( subBet.getConcluded() == Bets.Conclusion.IN_PROGRESS ) {
                allBetsConcluded = false;
            } else if ( subBet.getConcluded() == Bets.Conclusion.LOSS ) {
                allBetsWin = false;
            } else if ( subBet.getConcluded() == Bets.Conclusion.PUSH ) {
                pushConclusion = true;
            }
        }
        if ( allBetsConcluded ) {
            if ( allBetsWin ) {
                if ( pushConclusion ) {
                    bet.setConcluded( UserBets.Conclusion.PUSH );
                    betsServiceHelper.creditUser( user, bet.getAmount(), bet.getId(), "bet push refund" );
                } else {
                    bet.setConcluded( UserBets.Conclusion.WIN );
                    betsServiceHelper.creditUser( user, bet.getAmount()*bet.getOdds(), bet.getId(), "bet win" );
                }
            } else {
                bet.setConcluded( UserBets.Conclusion.LOSS );
            }
        } else {
            bet.setConcluded( UserBets.Conclusion.PARTIAL );
        }
    }
}
