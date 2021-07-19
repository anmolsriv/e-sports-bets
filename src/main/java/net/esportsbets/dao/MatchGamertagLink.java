package net.esportsbets.dao;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
class MatchGamertagLinkId implements Serializable {
    private String matchId;
    private String gamertag;
}

@Entity
@Getter
@Setter
@ToString
@Table(name = "match_gamertag_link")
@IdClass(MatchGamertagLinkId.class)
public class MatchGamertagLink {

    @Id
    @Column(name = "match_id")
    private String matchId;

    @Id
    @Column(name = "gamertag")
    private String gamertag;

    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "team_name")
    private String teamName;
}
