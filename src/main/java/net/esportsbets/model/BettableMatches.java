package net.esportsbets.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.esportsbets.dao.Matches;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class BettableMatches {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss", timezone = "GMT-5")
    public String matchId;
    public String team_1;
    public String team_0;
    public String time;

}
