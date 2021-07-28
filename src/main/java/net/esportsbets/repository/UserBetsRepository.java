package net.esportsbets.repository;

import net.esportsbets.dao.UserBets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBetsRepository extends JpaRepository<UserBets, Integer> {
}
