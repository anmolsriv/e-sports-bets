package net.esportsbets.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "matches")
public class Matches {
    @Id
    @Column(name = "match_id")
    private String matchId;

    @Column(name = "winner")
    private Integer winner;

    @Column(name = "map")
    private String map;

    @Column(name = "game_variant")
    private String gameVariant;

    @Column(name = "time")
    private Timestamp time;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "match_id", referencedColumnName = "match_id")
    private List<MatchScores> matchScores = new ArrayList<>();
}
