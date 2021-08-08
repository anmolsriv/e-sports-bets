package net.esportsbets.repository.hibernate;

import net.esportsbets.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Repository
public class MatchHibernateRepository {

    @Autowired
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Matches> getMatchesAfterTime(@NonNull List<String> matchId, @NonNull Timestamp timeStart) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Matches> matchesSearchQuery = criteriaBuilder.createQuery(Matches.class);
        Root<Matches> matches = matchesSearchQuery.from(Matches.class);
        Fetch<Matches, MatchScores> scoresJoin= matches.fetch("matchScores", JoinType.INNER);
        Fetch<MatchScores, MatchGamertagLink> gamertagLinkJoin= scoresJoin.fetch("matchGamertagLink", JoinType.INNER);
        Predicate timeClause = criteriaBuilder.greaterThan(matches.get("time"), timeStart);
        Predicate matchIdClause = matches.get("matchId").in(matchId);
        matchesSearchQuery.where(matchIdClause, timeClause);
        TypedQuery<Matches> query = entityManager.createQuery(matchesSearchQuery);
        return query.getResultList();

    }

    @Transactional(readOnly = true)
    public List<Matches> customMatchSearchQuery(@NonNull Timestamp timeStart, @NonNull Timestamp timeEnd, Pageable page) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Matches> matchesSearchQuery = criteriaBuilder.createQuery(Matches.class);
        Root<Matches> matches = matchesSearchQuery.from(Matches.class);
        Fetch<Matches, MatchScores> scoresJoin= matches.fetch("matchScores", JoinType.INNER);
        Fetch<MatchScores, MatchGamertagLink> gamertagLinkJoin= scoresJoin.fetch("matchGamertagLink", JoinType.INNER);
        Predicate timeQuery = criteriaBuilder.between(matches.get("time"), timeStart, timeEnd);
        matchesSearchQuery.where(timeQuery);
        Order timeOrder = criteriaBuilder.desc(matches.get("time"));
        matchesSearchQuery.orderBy(timeOrder);
        TypedQuery<Matches> query = entityManager.createQuery(matchesSearchQuery);
        query.setFirstResult((int)page.getOffset());
        query.setMaxResults(page.getPageSize());
        return query.getResultList();

    }

    @Transactional()
    @Async
    public void updateMatchesSpread( @NonNull List<Matches> matches ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        matches.forEach(match -> {
            match.getMatchScores().forEach(matchScore -> {
                CriteriaUpdate<MatchScores> criteriaMatchScore = builder.createCriteriaUpdate(MatchScores.class);
                Root<MatchScores> scoresRoot = criteriaMatchScore.from(MatchScores.class);
                criteriaMatchScore.set(scoresRoot.get("spread"), matchScore.getSpread());
                criteriaMatchScore.where(builder.equal(scoresRoot.get("matchId"), matchScore.getMatchId()),
                        builder.equal(scoresRoot.get("teamId"), matchScore.getTeamId()));
                entityManager.createQuery(criteriaMatchScore).executeUpdate();
            });
        } );
        entityManager.flush();
    }

}
