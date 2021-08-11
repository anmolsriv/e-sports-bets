package net.esportsbets.dao;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class UserBetsDetailsLockId implements Serializable {
    private Long userId;
    private String matchId;
}
