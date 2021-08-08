package net.esportsbets.service;

import net.esportsbets.dao.*;
import net.esportsbets.model.BettableMatches;
import net.esportsbets.model.MatchResults;
import net.esportsbets.repository.*;
import net.esportsbets.repository.hibernate.MatchHibernateRepository;
import net.esportsbets.service.helper.MatchInfoServiceHelper;
import org.pmml4s.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MatchInfoService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchHibernateRepository matchHibernateRepository;

    @Autowired
    private MatchOddsAndSpreadCalculationService mlService;

    @Autowired
    private BettableRepository bettableRepository;

    @Autowired
    private MatchInfoServiceHelper matchInfoServiceHelper;



    public List<MatchResults> getPastMatches( int pageNumber ) {

        Pageable page = PageRequest.of(pageNumber, 10);
        List<Matches> matches = matchHibernateRepository.customMatchSearchQuery(
                new Timestamp( new java.util.Date().getTime() - 12*60*60*1000 ),
                new Timestamp( new java.util.Date().getTime() ),
                page);

        updateSpreadForMatches( matches );

        return matches.stream()
                        .map(MatchResults::mapMatchResults)
                        .collect(Collectors.toList());
    }

    public int getPastMatchesPageCount() {

        int matches = matchRepository.countByTimeIsBetween(
                new Timestamp(new java.util.Date().getTime() - 12 * 60 * 60 * 1000),
                new Timestamp(new java.util.Date().getTime()));
        return matches / 10;
    }

    public List<BettableMatches> getBettableMatches( String userEmail ) {

        List<BettableMatchesdao> bettableMatches = (List<BettableMatchesdao>) bettableRepository.findAll();

        List<BettableMatches> matchResults = bettableMatches.stream()
                .map(BettableMatches::mapMatchResults)
                .collect(Collectors.toList());

        Map<String, String> matchInfo = matchResults.stream()
                                        .collect(Collectors.toMap(BettableMatches::getMatchId, BettableMatches::getGameVariant));
        Map<String, Pair<Double, Double>> resPredictions  = mlService.getPredictions(matchInfo);
        for (BettableMatches match : matchResults) {
            Pair<Double, Double> predictions = resPredictions.get(match.getMatchId());
            match.setTeam0WinOdds( new DecimalFormat("0.0#").format( 1 / predictions.getSecond() ) );
            match.setTeam1WinOdds( new DecimalFormat("0.0#").format( 1 / ( 1 - predictions.getSecond() ) ) );
            match.setTeam0Spread( new DecimalFormat("0.0#").format( -matchInfoServiceHelper.getNearestHalfPoint( predictions.getFirst() ) ) );
            match.setTeam1Spread( new DecimalFormat("0.0#").format( matchInfoServiceHelper.getNearestHalfPoint( predictions.getFirst() ) ) );
        }

        matchInfoServiceHelper.saveUserMatchesOdds( userEmail, matchResults );

        return matchResults;

    }

    public void updateSpreadForMatches( List<Matches> matches ) {
        List<Matches> updatableMatches = matches.stream()
                .filter( match -> {
                    MatchScores[] matchScores = match.getMatchScores().toArray(new MatchScores[0]);
                    return matchScores[0].getSpread() == null || matchScores[1].getSpread() == null;
                } )
                .collect(Collectors.toList());

        if ( updatableMatches.size() > 0 ) {
            Map<String, String> matchInfo = updatableMatches.stream()
                    .collect(Collectors.toMap(Matches::getMatchId, Matches::getGameVariant));
            Map<String, Pair<Double, Double>> resPredictions  = mlService.getPredictions(matchInfo);

            updatableMatches.forEach( match -> {
                Pair<Double, Double> predictions = resPredictions.get(match.getMatchId());
                MatchScores[] matchScores = match.getMatchScores().toArray(new MatchScores[0]);
                MatchScores team0 = matchScores[0].getTeamId().equals(0) ? matchScores[0] : matchScores[1];
                MatchScores team1 = matchScores[0].getTeamId().equals(0) ? matchScores[1] : matchScores[0];
                team0.setSpread( -matchInfoServiceHelper.getNearestHalfPoint( predictions.getFirst() ) );
                team1.setSpread( matchInfoServiceHelper.getNearestHalfPoint( predictions.getFirst() ) );
            } );
            matchHibernateRepository.updateMatchesSpread( updatableMatches );
        }
    }

}
