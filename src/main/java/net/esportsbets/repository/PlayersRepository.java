package net.esportsbets.repository;

import net.esportsbets.dao.Players;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PlayersRepository extends JpaRepository<Players, Long> {
    List<Players> findByGamertagIn(String[] gamertags);
    List<Players> findByGamertagStartingWithIgnoreCase(String searchString, Pageable pageable);
}
