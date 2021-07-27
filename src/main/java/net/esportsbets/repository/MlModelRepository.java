package net.esportsbets.repository;

import net.esportsbets.dao.MlModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MlModelRepository extends JpaRepository<MlModel, Long> {

    MlModel findByMatchId(String matchId);
}