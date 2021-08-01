package net.esportsbets.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserBetRequestModel {

    private String betType;
    private Double amount;
    private Double odds;
    private List<BetsRequestModel> bets;
}
