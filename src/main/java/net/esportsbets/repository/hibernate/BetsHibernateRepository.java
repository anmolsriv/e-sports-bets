package net.esportsbets.repository.hibernate;

import net.esportsbets.dao.*;
import org.hibernate.Criteria;
import org.hibernate.query.criteria.internal.predicate.InPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class BetsHibernateRepository {

    @Autowired
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Set<UserBets> customMatchSearchQuery(@NonNull Timestamp timeStart) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserBets> betsSearchQuery = criteriaBuilder.createQuery(UserBets.class);
        Root<UserBets> userBets = betsSearchQuery.from(UserBets.class);
        Fetch<UserBets, Bets> betsJoin = userBets.fetch("userBets", JoinType.INNER);
        Fetch<Bets, Matches> matchJoin = betsJoin.fetch("match", JoinType.INNER);
        Fetch<Matches, MatchScores> scoresJoin = matchJoin.fetch("matchScores", JoinType.INNER);
        Fetch<MatchScores, MatchGamertagLink> gamertagLinkJoin= scoresJoin.fetch("matchGamertagLink", JoinType.INNER);

        Predicate timeClause = criteriaBuilder.lessThan( ((Join<Bets, Matches>)matchJoin).get("time"), timeStart);

        CriteriaBuilder.In<UserBets.Conclusion> betStatusClause = criteriaBuilder.in( userBets.get("concluded") );
        betStatusClause.value( UserBets.Conclusion.IN_PROGRESS );
        betStatusClause.value( UserBets.Conclusion.PARTIAL );
        betsSearchQuery.where( betStatusClause, timeClause );
        TypedQuery<UserBets> query = entityManager.createQuery( betsSearchQuery );
        return new HashSet<UserBets>( query.getResultList() );
    }

    @Transactional()
    public void updateUserBetsConclusion( @NonNull Set<UserBets> userBets ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        userBets.forEach(userBet -> {
            CriteriaUpdate<UserBets> criteria = builder.createCriteriaUpdate(UserBets.class);
            Root<UserBets> root = criteria.from(UserBets.class);
            criteria.set(root.get("concluded"), userBet.getConcluded());
            criteria.where(builder.equal(root.get("id"), userBet.getId()));
            entityManager.createQuery(criteria).executeUpdate();
            userBet.getUserBets().forEach(bet -> {
                CriteriaUpdate<Bets> criteriaBet = builder.createCriteriaUpdate(Bets.class);
                Root<Bets> rootBet = criteriaBet.from(Bets.class);
                criteriaBet.set(rootBet.get("concluded"), bet.getConcluded());
                criteriaBet.where(builder.equal(rootBet.get("betId"), bet.getBetId()));
                entityManager.createQuery(criteriaBet).executeUpdate();
            });
        } );
        entityManager.flush();
    }

    @Transactional(readOnly = true)
    public Set<UserBets> getBetsForUser(Long userId) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserBets> betsSearchQuery = criteriaBuilder.createQuery(UserBets.class);
        Root<UserBets> userBets = betsSearchQuery.from(UserBets.class);
        Fetch<UserBets, Bets> betsJoin = userBets.fetch("userBets", JoinType.INNER);
        Fetch<Bets, Matches> matchJoin = betsJoin.fetch("match", JoinType.INNER);
        Fetch<Matches, MatchScores> scoresJoin = matchJoin.fetch("matchScores", JoinType.INNER);
        Fetch<MatchScores, MatchGamertagLink> gamertagLinkJoin= scoresJoin.fetch("matchGamertagLink", JoinType.INNER);

        Predicate userIdClause = criteriaBuilder.equal( userBets.get("userId"), userId );

        Order betStatusOrder = criteriaBuilder.asc( userBets.get("concluded") );
        Order timeOrder = criteriaBuilder.desc( ((Join<Bets, Matches>)matchJoin).get("time") );

        betsSearchQuery.where( userIdClause );
        betsSearchQuery.orderBy( betStatusOrder, timeOrder );

        TypedQuery<UserBets> query = entityManager.createQuery( betsSearchQuery );
        return new HashSet<UserBets>( query.getResultList() );
    }

}
