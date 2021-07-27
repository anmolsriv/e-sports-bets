package net.esportsbets.repository;

import net.esportsbets.dao.MlModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MlModelRepository extends JpaRepository<MlModel, Long> {

    List<MlModel> findByMatchIdIn(String[] matchIds);
}