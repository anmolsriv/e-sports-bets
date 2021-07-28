package net.esportsbets.dao;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.mapping.Join;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "matchScores")
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

    @OneToMany(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(name = "match_id", referencedColumnName = "match_id")
    private Set<MatchScores> matchScores;

}
