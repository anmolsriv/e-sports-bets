package net.esportsbets.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.esportsbets.dao.Bets;
import net.esportsbets.dao.UserBets;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
class BetsResponse {

    private Bets.BetType betType;
    private Integer teamId;
    private Double spread;
    private Bets.Conclusion concluded;
    private MatchResults match;

    static BetsResponse getInstance( Bets bet ) {
        BetsResponse response = new BetsResponse();
        response.setConcluded( bet.getConcluded() );
        response.setBetType( bet.getBetType() );
        response.setTeamId( bet.getTeamId() );
        response.setSpread( bet.getSpread() );
        response.setMatch( MatchResults.mapMatchResults( bet.getMatch() ) );
        if ( bet.getConcluded() == Bets.Conclusion.IN_PROGRESS ) {
            response.getMatch().getTeam1().setResult( null );
            response.getMatch().getTeam2().setResult( null );
            response.getMatch().getTeam1().setResult( null );
            response.getMatch().getTeam2().setResult( null );
        }
        return response;
    }
}

@Getter
@Setter
@ToString
public class UserBetsResponse {

    private UserBets.UserBetsComposition betsComposition;
    private UserBets.Conclusion concluded;
    private Double amount;
    private Double odds;
    private List<BetsResponse> userBets;

    public static UserBetsResponse getInstance( UserBets userBet ) {
        UserBetsResponse response = new UserBetsResponse();
        response.setBetsComposition( userBet.getBetsComposition() );
        response.setAmount( userBet.getAmount() );
        response.setOdds( userBet.getOdds() );
        response.setConcluded( userBet.getConcluded() );
        response.setUserBets( userBet.getUserBets().stream()
                                            .map(BetsResponse::getInstance)
                                            .collect(Collectors.toList()));
        return response;
    }
}
