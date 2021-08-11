package net.esportsbets.dao;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Table(name = "bettable_matches")
public class BettableMatchesdao implements Serializable {

  @Id
  @Column(name = "match_id")
  private String matchId;

  @Column(name = "map")
  private String map;

  @Column(name = "game_variant")
  private String gameVariant;

  @Column(name = "time")
  private Timestamp time;

  @Column(name = "team_1")
  private String team_1;

  @Column(name = "team_0")
  private String team_0;

}
