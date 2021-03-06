package net.esportsbets.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.esportsbets.dao.MatchScores;
import net.esportsbets.dao.Matches;
import net.esportsbets.service.helper.BetsServiceHelper;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
class TeamDetails {
    private String teamName;
    private String result;
    private Integer score;
    private Integer team;
    private Double spread;
    private String spreadResult;

    public static TeamDetails mapMatchResults(Matches match, int index, BetsServiceHelper betsServiceHelper) {

        MatchScores[] matchScores = match.getMatchScores().toArray(new MatchScores[0]);

        MatchScores matchScore = match.getMatchScores().stream()
                                            .filter(matchScoreTemp -> matchScoreTemp.getTeamId().equals(index) )
                                            .findFirst()
                                            .get();

        TeamDetails mappedTeam = new TeamDetails();
        mappedTeam.setTeamName( matchScore.getTeamName() );
        mappedTeam.setScore( matchScore.getScore() );
        mappedTeam.setResult( match.getWinner().equals(matchScore.getTeamId())?"Win":"Loss" );
        mappedTeam.setTeam( matchScore.getTeamId() );
        mappedTeam.setSpread( matchScore.getSpread() );
        mappedTeam.setSpreadResult( betsServiceHelper.processSpreadBet( matchScores, matchScore.getTeamId(), matchScore.getSpread() ).toString() );
        return mappedTeam;
    }
}

@Getter
@Setter
@ToString
public class MatchResults {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss", timezone = "GMT-5")
    private Timestamp time;
    private String map;
    private TeamDetails team1;
    private TeamDetails team2;
    private String gameVariant;

    public static MatchResults mapMatchResults(Matches match, BetsServiceHelper betsServiceHelper) {

        MatchResults mappedMatch = new MatchResults();
        mappedMatch.setTime( match.getTime() );
        mappedMatch.setMap( match.getMap() );
        mappedMatch.setGameVariant( match.getGameVariant() );
        mappedMatch.setTeam1( TeamDetails.mapMatchResults(match, 0, betsServiceHelper)  );
        mappedMatch.setTeam2( TeamDetails.mapMatchResults(match, 1, betsServiceHelper)  );

        return mappedMatch;
    }
}
