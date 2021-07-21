package net.esportsbets.repository;

import net.esportsbets.dao.MatchGamertagLink;
import net.esportsbets.dao.MatchScores;
import net.esportsbets.dao.Matches;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class MatchCustomRepository {

    @Autowired
    private EntityManager entityManager;

    public List<Matches> customMatchSearchQuery(@NonNull Timestamp timeStart, @NonNull Timestamp timeEnd, Pageable page) {
        String fields = "m.match_id as m_match_id, m.winner as m_winner, m.map as m_map, m.game_variant as m_game_variant, m.time as m_time, " +
                "ms.match_id as ms_match_id, ms.score as ms_score, ms.team_id as ms_team_id, " +
                "mgl.match_id as mgl_match_id, mgl.gamertag as mgl_gamertag, mgl.team_id as mgl_team_id, mgl.team_name as mgl_team_name";
        String queryName = "select " + fields +" from " +
                "(select match_id, winner, map, game_variant, time from matches where time between '" + timeStart + "' and '" + timeEnd + "' order by time desc limit "+ page.getOffset() +"," + page.getPageSize() + ") as m " +
                "inner join match_scores ms on m.match_id = ms.match_id " +
                "inner join match_gamertag_link mgl on ms.team_id = mgl.team_id and ms.match_id = mgl.match_id " +
                "order by m.time desc" ;
        Query query = entityManager.createNativeQuery( queryName );
        List<Object[]> result = query.getResultList();
        List<Matches> matchesList = new ArrayList<>();
        //mapping matches
        result.stream().forEach(record -> {
            Matches tempMatch = new Matches((String)record[0], (Integer)record[1], (String)record[2], (String)record[3], (Timestamp)record[4], new ArrayList<>());
            MatchScores tempMatchScore = new MatchScores((String)record[5], (Integer)record[6], (Integer)record[7], new ArrayList<>());
            MatchGamertagLink tempMatchGamertagLink = new MatchGamertagLink((String)record[8], (String)record[9], (Integer)record[10], (String)record[11]);
            if(!matchesList.contains(tempMatch)) {
                matchesList.add( tempMatch );
                tempMatch.getMatchScores().add(tempMatchScore);
                tempMatchScore.getMatchGamertagLink().add(tempMatchGamertagLink);
            } else {
                tempMatch = matchesList.get(matchesList.indexOf(tempMatch));
                if(!tempMatch.getMatchScores().contains(tempMatchScore)) {
                    tempMatch.getMatchScores().add(tempMatchScore);
                    tempMatchScore.getMatchGamertagLink().add(tempMatchGamertagLink);
                } else {
                    tempMatchScore = tempMatch.getMatchScores().get(tempMatch.getMatchScores().indexOf(tempMatchScore));
                    tempMatchScore.getMatchGamertagLink().add(tempMatchGamertagLink);
                }
            }
        });
        return matchesList;
    }

    public Integer customMatchesCount(@NonNull Timestamp timeStart, @NonNull Timestamp timeEnd) {
        String queryName = "select count(*) from matches where time between '" + timeStart + "' and '" + timeEnd + "'";
        Query query = entityManager.createNativeQuery( queryName );
        BigInteger result = (BigInteger) query.getSingleResult();

        return result.intValue();
    }
}
