package net.esportsbets.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "players")
@Getter
@Setter
@ToString
public class Players {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "gamertag")
    private String gamertag;

    @Column(name = "highest_csr_tier")
    private Integer highestCsrTier;

    @Column(name = "highest_csr_designation")
    private Integer highestCsrDesignation;

    @Column(name = "highest_csr")
    private Integer highestCsr;

    @Column(name = "highest_rank")
    private Integer highestRank;

    @Column(name = "csr_tier")
    private Integer csrTier;

    @Column(name = "csr_designation")
    private Integer csrDesignation;

    @Column(name = "csr")
    private Integer csr;

    @Column(name = "player_rank")
    private Integer playerRank;

    @Column(name = "csr_percentile")
    private Integer csrPercentile;

    @Column(name = "kills")
    private Integer kills;

    @Column(name = "headshots")
    private Integer headshots;

    @Column(name = "weapon_damage")
    private Float weaponDamage;

    @Column(name = "shots_fired")
    private Integer shotsFired;

    @Column(name = "shots_landed")
    private Integer shotsLanded;

    @Column(name = "grenade_damage")
    private Float grenadeDamage;

    @Column(name = "power_weapon_kills")
    private Integer powerWeaponKills;

    @Column(name = "power_weapon_damage")
    private Float powerWeaponDamage;

    @Column(name = "power_weapon_grabs")
    private Integer powerWeaponGrabs;

    @Column(name = "power_weapon_possession_time")
    private String powerWeaponPossessionTime;

    @Column(name = "deaths")
    private Integer deaths;

    @Column(name = "assists")
    private Integer assists;

    @Column(name = "games_completed")
    private Integer gamesCompleted;

    @Column(name = "games_won")
    private Integer gamesWon;

    @Column(name = "games_lost")
    private Integer gamesLost;

    @Column(name = "games_ties")
    private Integer gamesTies;

    @Column(name = "time_played")
    private String timePlayed;

    @Column(name = "grenade_kills")
    private Integer grenadeKills;

    @Column(name = "triple_double")
    private Integer tripleDouble;

    @Column(name = "top_gun")
    private Integer topGun;

    @Column(name = "perfect_kill")
    private Integer perfectKill;

    @Column(name = "reversal")
    private Integer reversal;

    @Column(name = "killing_spree")
    private Integer killingSpree;

    @Column(name = "wingman")
    private Integer wingman;

    @Column(name = "magnum_shots_fired")
    private Integer magnumShotsFired;

    @Column(name = "magnum_shots_landed")
    private Integer magnumShotsLanded;

    @Column(name = "magnum_headshots")
    private Integer magnumHeadshots;

    @Column(name = "magnum_kills")
    private Integer magnumKills;

    @Column(name = "magnum_damage")
    private Float magnumDamage;

    @Column(name = "magnum_possession_time")
    private String magnumPossessionTime;

    @Column(name = "sniper_shots_fired")
    private Integer sniperShotsFired;

    @Column(name = "sniper_shots_landed")
    private Integer sniperShotsLanded;

    @Column(name = "sniper_headshots")
    private Integer sniperHeadshots;

    @Column(name = "sniper_kills")
    private Integer sniperKills;

    @Column(name = "sniper_damage")
    private Float sniperDamage;

    @Column(name = "sniper_possession_time")
    private String  sniperPossessionTime;
}
