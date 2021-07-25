package net.esportsbets.service;

import net.esportsbets.ESportsBetsApplication;
import net.esportsbets.dao.MlModel;
import net.esportsbets.repository.MlModelRepository;
import org.pmml4s.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OddsModelingService {
    @Autowired
    private MlModelRepository mlModelRepo;

    private final Model moneylineModel = Model.fromFile(
            ESportsBetsApplication.class.getClassLoader().getResource(
                    "public/pmml/moneyline_bets.pmml").getFile());

    private final Model slayerSpreadModel = Model.fromFile(
            ESportsBetsApplication.class.getClassLoader().getResource(
                    "public/pmml/slayer_spread.pmml").getFile());

    private final Model strongholdSpreadModel = Model.fromFile(
            ESportsBetsApplication.class.getClassLoader().getResource(
                    "public/pmml/stronghold_spread.pmml").getFile());

    private final Model oddballSpreadModel = Model.fromFile(
            ESportsBetsApplication.class.getClassLoader().getResource(
                    "public/pmml/oddball_spread.pmml").getFile());

    private final Model ctfSpreadModel = Model.fromFile(
            ESportsBetsApplication.class.getClassLoader().getResource(
                    "public/pmml/ctf_spread.pmml").getFile());

    public Map<String, List<Double>> loadOddsForMatches(
            String[] matchIds, String betType) {
        List<MlModel> modelData = loadModelDataByMatchIds(matchIds);
        Map<String,  List<Double>> matchOdds = new HashMap<>();
        for (MlModel matchData : modelData) {
            Map<String, Float> values = getModelInputs(betType, matchData);
            Model model = getModelForBetType(betType);
            double prediction = getModelPrediction(model, values);
            List<Double> teamOdds;
            if (betType == "moneyline") {
                teamOdds = Arrays.asList(1 / prediction, 1 / (1 - prediction));
            } else {
                teamOdds = Arrays.asList(-getNearestHalfPoint(prediction),
                        getNearestHalfPoint(prediction));
            }

            matchOdds.put(matchData.getMatchId(), teamOdds);
        }
        return matchOdds;
    }

    private final List<MlModel> loadModelDataByMatchIds(
            String[] matchIds) {
        List<MlModel> modelData = mlModelRepo.findByMatchIdIn(matchIds);
        if (modelData == null) {
            return new ArrayList<MlModel>();
        }
        return modelData;
    }

    private final Double getModelPrediction(Model model, Map<String,
            Float> values) {
        Object[] valuesMap = Arrays.stream(model.inputNames())
                .map(values::get)
                .toArray();

        Object[] result = model.predict(valuesMap);
        return (Double) result[0];
    }

    private final Map<String, Float> getModelInputs(String betType,
                                                    MlModel matchData) {
        if (betType == "slayerSpread") {
            return Map.of(
                    "csr_percentile", matchData.getCsrPercentile(),
                    "kda", matchData.getKda(),
                    "top_gun_freq", matchData.getTopGunFreq(),
                    "triple_double_freq", matchData.getTripleDoubleFreq(),
                    "win_perc", matchData.getWinPerc()
            );
        } else if (betType == "strongholdSpread" ||
                betType == "oddballSpread") {
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

    private final Model getModelForBetType(String betType) {
        switch (betType) {
            case "slayerSpread":
                return slayerSpreadModel;

            case "strongholdSpread":
                return strongholdSpreadModel;

            case "oddballSpread":
                return oddballSpreadModel;

            case "ctfSpread":
                return ctfSpreadModel;

            default:
                return moneylineModel;
        }
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
