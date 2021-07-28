package net.esportsbets.dao;

import io.micrometer.core.annotation.Counted;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "bets")
@EqualsAndHashCode(exclude = "match")
public class Bets {

    public enum BetType {
        SPREAD, WIN;
    }

    public enum Conclusion {
        IN_PROGRESS, WIN, LOSS, PUSH;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bet_id")
    private Integer betId;

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "bet_type")
    @Enumerated(EnumType.STRING)
    private BetType betType;

    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "user_bet_id")
    private Integer userBetId;

    @Column(name = "spread")
    private Double spread;

    @Column(name = "concluded")
    private Conclusion concluded;

    @OneToOne(cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(name = "match_id", referencedColumnName = "match_id",
                        insertable = false, updatable = false)
    private Matches match;

}
