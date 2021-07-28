package net.esportsbets.service;

import net.esportsbets.dao.BettableMatchesdao;
import net.esportsbets.dao.Matches;
import net.esportsbets.dao.MlModel;
import net.esportsbets.model.BettableMatches;
import net.esportsbets.model.MatchResults;
import net.esportsbets.repository.hibernate.MatchHibernateRepository;
import net.esportsbets.repository.BettableRepository;
import net.esportsbets.repository.MatchRepository;
import net.esportsbets.repository.MlModelRepository;
import org.pmml4s.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchInfoService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchHibernateRepository matchHibernateRepository;

    @Autowired
    private MatchOddsAndSpreadCalculationService mlService;

    public List<MatchResults> getPastMatches( int pageNumber ) {

        Pageable page = PageRequest.of(pageNumber, 10);
        List<Matches> matches = matchHibernateRepository.customMatchSearchQuery(
                new Timestamp( new java.util.Date().getTime() - 12*60*60*1000 ),
                new Timestamp( new java.util.Date().getTime() ),
                page);

        List<MatchResults> matchResults = matches.stream()
                                                    .map( match -> MatchResults.mapMatchResults(match) )
                                                    .collect(Collectors.toList());

        return matchResults;
    }

    public int getPastMatchesPageCount() {

        int matches = matchRepository.countByTimeIsBetween(
                                                        new Timestamp( new java.util.Date().getTime() - 12*60*60*1000 ),
                                                        new Timestamp( new java.util.Date().getTime() ));
        return matches/10;
    }

    @Autowired
    private BettableRepository bettableRepository;

    public List<BettableMatches> getBettableMatches() {

        List<BettableMatchesdao> bettableMatches = (List<BettableMatchesdao>) bettableRepository.findAll();

        List<BettableMatches> matchResults = bettableMatches.stream()
                .map( match -> BettableMatches.mapMatchResults(match))
                .collect(Collectors.toList());

        for (BettableMatches match : matchResults) {
            MlModel matchData = mlService.loadModelDataByMatchId(match.getMatchId());
            Map<String, Float> spreadInputs = mlService.getModelInputs( match.getGameVariant(), matchData );
            Map<String, Float> moneylineInputs = mlService.getModelInputs( "", matchData );
            Model spreadModel = mlService.getSpreadModelForGameVariant( match.getGameVariant() );
            Model moneylineModel = mlService.getSpreadModelForGameVariant( "" );
            double spreadPrediction = mlService.getModelPrediction( spreadModel, spreadInputs );
            double moneylinePrediction = mlService.getModelPrediction( moneylineModel, moneylineInputs );
            match.setTeam0WinOdds( new DecimalFormat("0.0#").format( 1 / moneylinePrediction ) );
            match.setTeam1WinOdds( new DecimalFormat("0.0#").format( 1 / (1 - moneylinePrediction) ) );
            match.setTeam0Spread( new DecimalFormat("0.0#").format( -getNearestHalfPoint(spreadPrediction) ) );
            match.setTeam1Spread( new DecimalFormat("0.0#").format( getNearestHalfPoint(spreadPrediction) ) );
        }
        return matchResults;

    }

    private final double getNearestHalfPoint(Double point) {
        double pointRoundToHalf = Math.round(point * 2) / 2.0;
        if (pointRoundToHalf % 1 == 0) {
            if (pointRoundToHalf >= point) {
                return pointRoundToHalf - 0.5;
            } else {
                return pointRoundToHalf + 0.5;
            }
        }
        return pointRoundToHalf;
    }

}
