package net.esportsbets.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchOddsResponse {

    private String matchID;
    private String team0WinOdds;
    private String team1WinOdds;
    private String team0Spread;
    private String team1Spread;
}
