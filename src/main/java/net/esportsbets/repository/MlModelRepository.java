package net.esportsbets.repository;

import net.esportsbets.dao.MlModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MlModelRepository extends JpaRepository<MlModel, Long> {

    MlModel findByMatchId(String matchId);

    List<MlModel> findByMatchIdIn(Set<String> matchIds);
}