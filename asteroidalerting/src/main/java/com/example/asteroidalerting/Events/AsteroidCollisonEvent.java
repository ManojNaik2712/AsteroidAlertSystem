package com.example.asteroidalerting.Events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AsteroidCollisonEvent {
    private String asteroidName;
    private String closeApproachDate;
    private String missdistanceKilometer;
    private double estimatedDiameterAvgMeter;

}
