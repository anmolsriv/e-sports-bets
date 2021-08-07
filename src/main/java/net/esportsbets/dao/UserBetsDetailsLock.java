package net.esportsbets.dao;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "user_bets_details_lock")
@IdClass(UserBetsDetailsLockId.class)
public class UserBetsDetailsLock {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "match_id")
    private String matchId;

    @Column(name = "team_0_spread")
    private Double team0Spread;

    @Column(name = "team_1_spread")
    private Double team1Spread;

    @Column(name = "team_0_moneyline_odds")
    private Double team0MoneylineOdds;

    @Column(name = "team_1_moneyline_odds")
    private Double team1MoneylineOdds;

    @Column(name = "team_0_spread_odds")
    private Double team0SpreadOdds;

    @Column(name = "team_1_spread_odds")
    private Double team1SpreadOdds;

}
