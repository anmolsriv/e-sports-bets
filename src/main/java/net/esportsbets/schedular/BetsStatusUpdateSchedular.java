package net.esportsbets.schedular;

import net.esportsbets.dao.Bets;
import net.esportsbets.dao.MatchScores;
import net.esportsbets.dao.User;
import net.esportsbets.dao.UserBets;
import net.esportsbets.repository.UserBetsRepository;
import net.esportsbets.repository.UserRepository;
import net.esportsbets.repository.hibernate.BetsHibernateRepository;
import net.esportsbets.service.BetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BetsStatusUpdateSchedular {

    @Autowired
    private BetsHibernateRepository betsHibernateRepository;

    @Autowired
    private BetsService betsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBetsRepository userBetsRepository;

    @Scheduled(cron = "0 1/30 * 1/1 * ?")
    public void computeBetResults() {

        Timestamp currTime = new Timestamp( new java.util.Date().getTime() );
        List<UserBets> pendingBets = betsHibernateRepository.customMatchSearchQuery( currTime );
        pendingBets.forEach(pendingBet -> processBetResults(pendingBet, currTime) );
        userBetsRepository.saveAll( pendingBets );
    }

    private void processSpreadBet( User user, UserBets bet, Bets subBet ) {
        MatchScores[] matchScores = (MatchScores[]) subBet.getMatch().getMatchScores().toArray();
        int scoresDiff = matchScores[0].getScore() - matchScores[1].getScore();
        if ( matchScores[1].getTeamId() == subBet.getTeamId() ) {
            scoresDiff *= -1;
        }
        if ( subBet.getSpread()<0 ) {
            if ( -1*subBet.getSpread() == scoresDiff ) {
                subBet.setConcluded( Bets.Conclusion.PUSH );
                betsService.creditUser( user, bet.getAmount(), bet.getId(), "spread bet push" );
            } else if (-1*subBet.getSpread() > scoresDiff) {
                subBet.setConcluded( Bets.Conclusion.WIN );
            }
        } else if ( subBet.getSpread()>0 ) {
            if ( subBet.getSpread() == scoresDiff ) {
                subBet.setConcluded( Bets.Conclusion.PUSH );
                betsService.creditUser( user, bet.getAmount(), bet.getId(), "spread bet push" );
            } else if ( subBet.getSpread() > Math.abs(scoresDiff) ) {
                subBet.setConcluded( Bets.Conclusion.WIN );
            }
        }

        if ( subBet.getConcluded() == Bets.Conclusion.LOSS ) {
            betsService.debitUser( user, 10.0, bet.getId(), "spread bet loss" );
        }
    }

    private void processBetResults( UserBets bet, Timestamp currTime ) {

        User user = userRepository.findById( bet.getUserId() ).get();
        for (Bets subBet : bet.getUserBets()) {
            if ( subBet.getConcluded() == Bets.Conclusion.IN_PROGRESS &&
                    subBet.getMatch().getTime().before( currTime ) ) {
                subBet.setConcluded( Bets.Conclusion.LOSS );
                if ( subBet.getMatch().getWinner().equals( subBet.getTeamId() ) ) {
                    if ( subBet.getBetType() == Bets.BetType.SPREAD ) {
                        processSpreadBet( user, bet, subBet );
                    } else {
                        subBet.setConcluded( Bets.Conclusion.WIN );
                    }
                }
            }
        }
        boolean allBetsConcluded = true;
        boolean allBetsWin = true;
        for (Bets subBet : bet.getUserBets()) {
            if ( subBet.getConcluded() == Bets.Conclusion.IN_PROGRESS ) {
                allBetsConcluded = false;
            } else if ( subBet.getConcluded() == Bets.Conclusion.LOSS ) {
                allBetsWin = false;
            }
        }
        if ( allBetsConcluded ) {
            if ( allBetsWin ) {
                bet.setConcluded( UserBets.Conclusion.WIN );
                betsService.creditUser( user, bet.getAmount()*bet.getOdds(), bet.getId(), "bet win" );
            } else {
                bet.setConcluded( UserBets.Conclusion.LOSS );
            }
        } else {
            bet.setConcluded( UserBets.Conclusion.PARTIAL );
        }
    }
}
