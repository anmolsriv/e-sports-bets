package net.esportsbets.repository;

import net.esportsbets.dao.MatchGamertagLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchGamertagLinkRepository extends JpaRepository<MatchGamertagLink, Long> {

    List<MatchGamertagLink> findGamertagByMatchIdAndTeamId(String matchId, Integer teamName);

}
