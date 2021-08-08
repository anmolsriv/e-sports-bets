package net.esportsbets.repository;

import net.esportsbets.dao.Matches;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MatchRepository extends PagingAndSortingRepository<Matches, Long> {

    List<Matches> findByTimeIsBetweenOrderByTimeDesc(@NonNull Timestamp timeStart, @NonNull Timestamp timeEnd, Pageable page);

    List<Matches> findOneByTimeIsBetweenOrderByTimeDesc(@NonNull Timestamp timeStart, @NonNull Timestamp timeEnd, Pageable page);

    int countByTimeIsBetween(@NonNull Timestamp timeStart, @NonNull Timestamp timeEnd);
}
