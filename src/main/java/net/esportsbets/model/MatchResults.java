package net.esportsbets.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.esportsbets.dao.MatchGamertagLink;
import net.esportsbets.dao.MatchScores;
import net.esportsbets.dao.Matches;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

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
        mappedTeam.setTeamName( "" );
        for (MatchGamertagLink matchGamertagLink : matchScore.getMatchGamertagLink()) {
            if (!matchGamertagLink.getTeamName().isBlank()) {
                mappedTeam.setTeamName( matchGamertagLink.getTeamName() );
                break;
            }
        }
        mappedTeam.setScore( matchScore.getScore() );
        mappedTeam.setResult( match.getWinner().equals(matchScore.getTeamId())?"Win":"Loss" );
        return mappedTeam;
    }
}

@Getter
@Setter
@ToString
public class MatchResults {

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM-dd-yyyy HH:mm:ss", timezone="GMT-5")
    private Timestamp time;
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
