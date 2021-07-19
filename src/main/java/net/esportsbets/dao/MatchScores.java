package net.esportsbets.dao;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumns( {@JoinColumn(name = "match_id", referencedColumnName = "match_id"),
                    @JoinColumn(name = "team_id", referencedColumnName = "team_id")} )
    private List<MatchGamertagLink> matchGamertagLink;

}
