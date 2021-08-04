package net.esportsbets.repository;

import net.esportsbets.dao.Players;
import net.esportsbets.model.CustomPlayerStats;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayersRepository extends JpaRepository<Players, Long> {

    List<CustomPlayerStats> findByGamertagIn(String[] gamertags);

    List<CustomPlayerStats> findByGamertagStartingWithIgnoreCase(
            String searchString, Pageable pageable);

    @Query(value = "SELECT p FROM Players p WHERE p.gamesLost + p.gamesWon > 20 ORDER BY p.kills DESC")
    List<CustomPlayerStats> findTopPlayersByKills(Pageable playerLimit);

    @Query(value = "SELECT p FROM Players p WHERE p.gamesLost + p.gamesWon > 20 ORDER BY p.deaths DESC")
    List<CustomPlayerStats> findTopPlayersByDeaths(Pageable playerLimit);

    @Query(value = "SELECT p FROM Players p WHERE p.gamesLost + p.gamesWon > 20 ORDER BY p.assists DESC")
    List<CustomPlayerStats> findTopPlayersByAssists(Pageable playerLimit);

    @Query(value = "SELECT p FROM Players p WHERE p.gamesLost + p.gamesWon > 20 ORDER BY p.weaponDamage DESC")
    List<CustomPlayerStats> findTopPlayersByWeaponDamage(Pageable playerLimit);

    @Query(value = "SELECT p FROM Players p WHERE p.gamesLost + p.gamesWon > 20 ORDER BY (p.kills + p.assists / 3) / p.deaths DESC")
    List<CustomPlayerStats> findTopPlayersByKDA(Pageable playerLimit);

    @Query(value = "SELECT p FROM Players p WHERE p.gamesLost + p.gamesWon > 20 ORDER BY p.gamesWon / p.gamesLost DESC")
    List<CustomPlayerStats> findTopPlayersByWinPercentage(Pageable playerLimit);

    @Query(value = "SELECT p FROM Players p WHERE p.gamesLost + p.gamesWon > 20 ORDER BY p.shotsLanded / p.shotsFired DESC")
    List<CustomPlayerStats> findTopPlayersByAccuracy(Pageable playerLimit);

    @Query(value = "SELECT p FROM Players p WHERE p.gamesLost + p.gamesWon > 20 ORDER BY p.perfectKill DESC")
    List<CustomPlayerStats> findTopPlayersByPerfectKill(Pageable playerLimit);

    @Query(value = "SELECT p FROM Players p WHERE p.gamesLost + p.gamesWon > 20 ORDER BY p.powerWeaponKills DESC")
    List<CustomPlayerStats> findTopPlayersByPowerWeaponKills(Pageable playerLimit);
}
