package net.esportsbets.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "ml_model")
@Getter
@Setter
@ToString
public class MlModel {
    @Id
    @Column(name = "match_id")
    private String matchId;

    @Column(name = "csr_percentile")
    private Float csrPercentile;

    @Column(name = "kda")
    private Float kda;

    @Column(name = "magnum_acc")
    private Float magnumAcc;

    @Column(name = "top_gun_freq")
    private Float topGunFreq;

    @Column(name = "triple_double_freq")
    private Float tripleDoubleFreq;

    @Column(name = "win_perc")
    private Float winPerc;
}
