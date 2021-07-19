package net.esportsbets.repository;

import net.esportsbets.dao.Matches;
import net.esportsbets.dao.Players;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MatchRepository extends PagingAndSortingRepository<Matches, Long> {

    List<Matches> findByTimeIsBetweenOrderByTimeAsc(@NonNull Date timeStart, @NonNull Date timeEnd, Pageable page);

    int countByTimeIsBetweenOrderByTimeAsc(@NonNull Date timeStart, @NonNull Date timeEnd);
}
