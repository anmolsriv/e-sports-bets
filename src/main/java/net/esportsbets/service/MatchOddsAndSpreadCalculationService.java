package net.esportsbets.service;

import net.esportsbets.dao.MlModel;
import net.esportsbets.repository.MlModelRepository;
import org.pmml4s.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

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

    public MlModel loadModelDataByMatchId(
            String matchId) {
        MlModel modelData = mlModelRepo.findByMatchId(matchId);
        if (modelData == null) {
            return new MlModel();
        }
        return modelData;
    }

    public Double getModelPrediction(Model model, Map<String,
            Float> inputs) {
        Object[] valuesMap = Arrays.stream(model.inputNames())
                .map(inputs::get)
                .toArray();

        Object[] result = model.predict(valuesMap);
        return (Double) result[0];
    }

    public Map<String, Float> getModelInputs(String gameVariant,
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

    public Model getSpreadModelForGameVariant(String gameVariant) {
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
}
