package net.esportsbets.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"spread", "betType"})
public class BetsRequestModel {

    private String matchId;
    private String betType;
    private Integer teamId;
    private Double spread;
}
