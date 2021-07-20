package net.esportsbets.repository;

import net.esportsbets.dao.Matches;
import net.esportsbets.dao.Players;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MatchRepository extends PagingAndSortingRepository<Matches, Long> {

    List<Matches> findByTimeIsBetweenOrderByTimeAsc(@NonNull Timestamp timeStart, @NonNull Timestamp timeEnd, Pageable page);

    int countByTimeIsBetweenOrderByTimeAsc(@NonNull Timestamp timeStart, @NonNull Timestamp timeEnd);

}

