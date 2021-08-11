package net.esportsbets.service.helper;

import net.esportsbets.dao.User;
import net.esportsbets.dao.UserBetsDetailsLock;
import net.esportsbets.model.BettableMatches;
import net.esportsbets.repository.UserBetsDetailsLockRepository;
import net.esportsbets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchInfoServiceHelper {

    @Autowired
    private UserBetsDetailsLockRepository userBetsDetailsLockRepository;

    @Autowired
    private UserRepository userRepository;

    @Async
    public void saveUserMatchesOdds ( String userEmail, List<BettableMatches> bettableMatches ) {

        User user = userRepository.findByEmail( userEmail );

        List<UserBetsDetailsLock> userBetsDetailsList = bettableMatches.stream()
                .map( match -> {
                    UserBetsDetailsLock userBetsDetails = new UserBetsDetailsLock();
                    userBetsDetails.setUserId( user.getId() );
                    userBetsDetails.setMatchId( match.getMatchId() );
                    userBetsDetails.setTeam0MoneylineOdds( Double.parseDouble( match.getTeam0WinOdds() ) );
                    userBetsDetails.setTeam1MoneylineOdds( Double.parseDouble( match.getTeam1WinOdds() ) );
                    userBetsDetails.setTeam0Spread( Double.parseDouble( match.getTeam0Spread() ) );
                    userBetsDetails.setTeam1Spread( Double.parseDouble( match.getTeam1Spread() ) );
                    userBetsDetails.setTeam0SpreadOdds( 1.91 );
                    userBetsDetails.setTeam1SpreadOdds( 1.91 );
                    return userBetsDetails;
                } )
                .collect(Collectors.toList());

        userBetsDetailsLockRepository.saveAll( userBetsDetailsList );
    }

    public double getNearestHalfPoint(Double point) {
        double pointRoundToHalf = Math.round(point * 2) / 2.0;
        if (pointRoundToHalf % 1 == 0) {
            if (pointRoundToHalf >= point) {
                return pointRoundToHalf - 0.5;
            } else {
                return pointRoundToHalf + 0.5;
            }
        }
        return pointRoundToHalf;
    }
}
