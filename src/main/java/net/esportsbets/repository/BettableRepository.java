package net.esportsbets.repository;

import net.esportsbets.dao.BettableMatchesdao;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BettableRepository extends PagingAndSortingRepository<BettableMatchesdao, String> {
}
