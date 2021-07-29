package net.esportsbets.service;

import net.esportsbets.dao.MlModel;
import net.esportsbets.repository.MlModelRepository;
import org.pmml4s.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MatchOddsAndSpreadCalculationService {

    @Autowired
    private MlModelRepository mlModelRepo;

    @Autowired
    @Qualifier("moneylineModel")
    private Model moneylineModel;

    @Autowired
    @Qualifier("slayerSpreadModel")
    private Model slayerSpreadModel;

    @Autowired
    @Qualifier("strongholdSpreadModel")
    private Model strongholdSpreadModel;

    @Autowired
    @Qualifier("oddballSpreadModel")
    private Model oddballSpreadModel;

    @Autowired
    @Qualifier("ctfSpreadModel")
    private Model ctfSpreadModel;

    private Map<String, MlModel> loadModelDataByMatchId(Set<String> matchIds) {
        List<MlModel> modelsData = mlModelRepo.findByMatchIdIn(matchIds);
        Map<String, MlModel> mlModels = modelsData.stream()
                                            .collect(Collectors.toMap(MlModel::getMatchId, Function.identity()));
        matchIds.stream().filter(matchId -> !mlModels.containsKey(matchId))
                            .forEach(matchId -> mlModels.put(matchId, new MlModel()));
        return mlModels;
    }

    private Double getModelPrediction(Model model, Map<String, Float> inputs) {
        Object[] valuesMap = Arrays.stream(model.inputNames())
                .map(inputs::get)
                .toArray();

        Object[] result = model.predict(valuesMap);
        return (Double) result[0];
    }

    private Map<String, Float> getModelInputs(String gameVariant,
                                                    MlModel matchData) {
        if (gameVariant == "Slayer HCS") {
            return Map.of(
                    "csr_percentile", matchData.getCsrPercentile(),
                    "kda", matchData.getKda(),
                    "top_gun_freq", matchData.getTopGunFreq(),
                    "triple_double_freq", matchData.getTripleDoubleFreq(),
                    "win_perc", matchData.getWinPerc()
            );
        } else if (gameVariant == "Strongholds HCS" ||
                gameVariant == "oddballSpread") {
            return Map.of(
                    "csr_percentile", matchData.getCsrPercentile(),
                    "top_gun_freq", matchData.getTopGunFreq(),
                    "win_perc", matchData.getWinPerc()
            );
        } else {
            return Map.of(
                    "csr_percentile", matchData.getCsrPercentile(),
                    "kda", matchData.getKda(),
                    "magnum_acc", matchData.getMagnumAcc(),
                    "top_gun_freq", matchData.getTopGunFreq(),
                    "triple_double_freq", matchData.getTripleDoubleFreq(),
                    "win_perc", matchData.getWinPerc()
            );
        }
    }

    private Model getSpreadModelForGameVariant(String gameVariant) {
        switch ( gameVariant ) {
            case "Slayer HCS":
                return slayerSpreadModel;

            case "Strongholds HCS":
                return strongholdSpreadModel;

            case "Oddball HCS":
                return oddballSpreadModel;

            case "": return moneylineModel;

            default:
                return ctfSpreadModel;
        }
    }

    /**
     * @param matchInfo : The Map(matchId, GameVariant)
     * @return : The Map(matchId, Pair(spreadPrediction, moneylinePrediction))
     * */
    public Map<String, Pair<Double, Double>> getPredictions( Map<String, String> matchInfo ) {
        Set<String> matchIds = matchInfo.keySet();
        Map<String, MlModel> matchesData = loadModelDataByMatchId( matchIds );
        Map<String, Pair<Double, Double>> resPredictions  = new HashMap<>();
        matchesData.forEach( (matchId, matchData) -> {
            Map<String, Float> spreadInputs = getModelInputs( matchInfo.get(matchId), matchData );
            Map<String, Float> moneylineInputs = getModelInputs( "", matchData );
            Model spreadModel = getSpreadModelForGameVariant( matchInfo.get(matchId) );
            Model moneylineModel = getSpreadModelForGameVariant( "" );
            double spreadPrediction = getModelPrediction( spreadModel, spreadInputs );
            double moneylinePrediction = getModelPrediction( moneylineModel, moneylineInputs );
            resPredictions.put(matchId, Pair.of( spreadPrediction, moneylinePrediction ));
        });
        return resPredictions;
    }
}
