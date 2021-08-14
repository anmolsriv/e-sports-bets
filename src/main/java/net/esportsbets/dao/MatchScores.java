package net.esportsbets.dao;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Embeddable
@Getter
@Setter
@EqualsAndHashCode
class MatchScoresId implements Serializable {
    private String matchId;
    private Integer teamId;
}

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "matchGamertagLink")
@Table(name = "match_scores")
@IdClass(MatchScoresId.class)
public class MatchScores {

    @Id
    @Column(name = "match_id")
    private String matchId;

    @Column(name = "score")
    private Integer score;

    @Id
    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "spread")
    private Double spread;

    @OneToMany(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.JOIN)
    @JoinColumns( {@JoinColumn(name = "match_id", referencedColumnName = "match_id"),
                    @JoinColumn(name = "team_id", referencedColumnName = "team_id")} )
    private Set<MatchGamertagLink> matchGamertagLink;

}
