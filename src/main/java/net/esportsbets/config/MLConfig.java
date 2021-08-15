package net.esportsbets.config;

import net.esportsbets.ESportsBetsApplication;
import org.pmml4s.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class MLConfig {

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean("moneylineModel")
    public Model moneylineModel() throws IOException {

       return Model.fromInputStream( resourceLoader.getResource("classpath:public/pmml/moneyline_bets.pmml").getInputStream() );
    }

    @Bean("slayerSpreadModel")
    public Model slayerSpreadModel() throws IOException {
        return Model.fromInputStream( resourceLoader.getResource("classpath:public/pmml/slayer_spread.pmml").getInputStream() );
    }

    @Bean("strongholdSpreadModel")
    public Model strongholdSpreadModel() throws IOException {
        return Model.fromInputStream( resourceLoader.getResource( "classpath:public/pmml/stronghold_spread.pmml").getInputStream() );
    }

    @Bean("oddballSpreadModel")
    public Model oddballSpreadModel() throws IOException {
        return Model.fromInputStream( resourceLoader.getResource( "classpath:public/pmml/oddball_spread.pmml").getInputStream() );
    }

    @Bean("ctfSpreadModel")
    public Model ctfSpreadModel() throws IOException {
        return Model.fromInputStream( resourceLoader.getResource( "classpath:public/pmml/ctf_spread.pmml").getInputStream() );
    }

}
