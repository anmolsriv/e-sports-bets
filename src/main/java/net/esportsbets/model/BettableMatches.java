package net.esportsbets.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.esportsbets.dao.BettableMatchesdao;
import net.esportsbets.dao.Matches;

import javax.persistence.Column;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class BettableMatches {

    private String matchId;
    private String map;
    private String gameVariant;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss", timezone = "GMT-5")
    private Timestamp time;
    private String team_1;
    private String team_0;

    public static BettableMatches mapMatchResults(BettableMatchesdao match) {

        BettableMatches mappedMatch = new BettableMatches();
        mappedMatch.setTime( match.getTime() );
        mappedMatch.setMap( match.getMap() );
        mappedMatch.setGameVariant( match.getGameVariant() );
        mappedMatch.setMatchId( match.getMatchId() );
        mappedMatch.setTeam_0( match.getTeam_0() );
        mappedMatch.setTeam_1( match.getTeam_1() );

        return mappedMatch;
    }

}
