package net.esportsbets.repository.hibernate;

import net.esportsbets.dao.*;
import org.hibernate.query.criteria.internal.predicate.InPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class BetsHibernateRepository {

    @Autowired
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<UserBets> customMatchSearchQuery(@NonNull Timestamp timeStart) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserBets> incompleteBetsSearchQuery = criteriaBuilder.createQuery(UserBets.class);
        Root<UserBets> userBets = incompleteBetsSearchQuery.from(UserBets.class);
        Fetch<UserBets, Bets> betsJoin = userBets.fetch("userBets", JoinType.INNER);
        Fetch<Bets, Matches> matchJoin = betsJoin.fetch("match", JoinType.INNER);
        Fetch<Matches, MatchScores> scoresJoin = matchJoin.fetch("matchScores", JoinType.INNER);
        Fetch<MatchScores, MatchGamertagLink> gamertagLinkJoin= scoresJoin.fetch("matchGamertagLink", JoinType.INNER);

        Predicate timeClause = criteriaBuilder.lessThan( ((Join<Bets, Matches>)matchJoin).get("time"), timeStart);

        CriteriaBuilder.In<Integer> betStatusClause = criteriaBuilder.in( userBets.get("concluded") );
        betStatusClause.value(0);
        betStatusClause.value(1);
        incompleteBetsSearchQuery.where(betStatusClause, timeClause);
        TypedQuery<UserBets> query = entityManager.createQuery(incompleteBetsSearchQuery);
        return query.getResultList();

    }
}
