package net.esportsbets.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_credits")
@Getter
@Setter
public class UserCredits {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "credits")
    private Double credits;
}
