package com.example.asteroidalerting.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiameterRange {
    @JsonProperty("estimated_diameter_min")
    private double minDiameter;

    @JsonProperty("estimated_diameter_max")
    private double maxDiameter;
}
