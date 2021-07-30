package net.esportsbets.dao;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user_bets")
@EqualsAndHashCode(exclude = "userBets")
public class UserBets {

    public enum UserBetsComposition {
        SINGLE, ACCUMULATOR;
    }

    public enum Conclusion {
        IN_PROGRESS, PARTIAL, WIN, LOSS, PUSH;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "bet_type")
    @Enumerated(EnumType.STRING)
    private UserBetsComposition betsComposition;

    @Column(name = "concluded")
    private Conclusion concluded;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "time", insertable = false)
    private java.sql.Timestamp time;

    @Column(name = "odds")
    private Double odds;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(name = "user_bet_id", referencedColumnName = "id")
    private Set<Bets> userBets;

}
