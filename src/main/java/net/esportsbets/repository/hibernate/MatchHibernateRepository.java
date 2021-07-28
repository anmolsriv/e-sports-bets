package net.esportsbets.repository.hibernate;

import net.esportsbets.dao.MatchGamertagLink;
import net.esportsbets.dao.MatchScores;
import net.esportsbets.dao.Matches;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class MatchHibernateRepository {

    @Autowired
    private EntityManager entityManager;

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

}
