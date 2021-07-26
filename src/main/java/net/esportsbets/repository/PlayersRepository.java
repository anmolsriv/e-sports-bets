package net.esportsbets.repository;

import net.esportsbets.dao.Players;
import net.esportsbets.model.CustomPlayerStats;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayersRepository extends JpaRepository<Players, Long> {

    List<CustomPlayerStats> findByGamertagIn(String[] gamertags);

    List<CustomPlayerStats> findByGamertagStartingWithIgnoreCase(
            String searchString, Pageable pageable);
}
