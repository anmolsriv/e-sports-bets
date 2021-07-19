package net.esportsbets.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.esportsbets.dao.MatchScores;
import net.esportsbets.dao.Matches;

import java.sql.Date;

@Getter
@Setter
@ToString
class TeamDetails {
    private String teamName;
    private String result;
    private Integer score;

    public static TeamDetails mapMatchResults(Matches match, int index) {

        MatchScores matchScore = match.getMatchScores().get(index);
        TeamDetails mappedTeam = new TeamDetails();
        mappedTeam.setTeamName( matchScore.getMatchGamertagLink().get(0).getTeamName() );
        mappedTeam.setScore( matchScore.getScore() );
        mappedTeam.setResult( match.getWinner().equals(matchScore.getTeamId())?"Win":"Loss" );
        return mappedTeam;
    }
}

@Getter
@Setter
@ToString
public class MatchResults {

    private Date time;
    private String map;
    private TeamDetails team1;
    private TeamDetails team2;

    public static MatchResults mapMatchResults(Matches match) {

        MatchResults mappedMatch = new MatchResults();
        mappedMatch.setTime( match.getTime() );
        mappedMatch.setMap( match.getMap() );
        mappedMatch.setTeam1( TeamDetails.mapMatchResults(match, 0)  );
        mappedMatch.setTeam2( TeamDetails.mapMatchResults(match, 1)  );
        return mappedMatch;
    }
}
