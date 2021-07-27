package net.esportsbets.config;

import net.esportsbets.ESportsBetsApplication;
import org.pmml4s.model.Model;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MlConfig {

    @Bean("moneylineModel")
    public Model moneylineModel() {
        return Model.fromFile(
                ESportsBetsApplication.class.getClassLoader().getResource(
                        "public/pmml/moneyline_bets.pmml").getFile());
    }

    @Bean("slayerSpreadModel")
    public Model slayerSpreadModel() {
        return Model.fromFile(
                ESportsBetsApplication.class.getClassLoader().getResource(
                        "public/pmml/slayer_spread.pmml").getFile());
    }

    @Bean("strongholdSpreadModel")
    public Model strongholdSpreadModel() {
        return Model.fromFile(
                ESportsBetsApplication.class.getClassLoader().getResource(
                        "public/pmml/stronghold_spread.pmml").getFile());
    }

    @Bean("oddballSpreadModel")
    public Model oddballSpreadModel() {
        return Model.fromFile(
                ESportsBetsApplication.class.getClassLoader().getResource(
                        "public/pmml/oddball_spread.pmml").getFile());
    }

    @Bean("ctfSpreadModel")
    public Model ctfSpreadModel() {
        return Model.fromFile(
                ESportsBetsApplication.class.getClassLoader().getResource(
                        "public/pmml/ctf_spread.pmml").getFile());
    }

}