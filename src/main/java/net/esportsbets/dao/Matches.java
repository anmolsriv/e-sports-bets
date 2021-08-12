package net.esportsbets.dao;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "matchScores")
@Table(name = "matches_demo")
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

    @OneToMany(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(name = "match_id", referencedColumnName = "match_id")
    private Set<MatchScores> matchScores;

}
