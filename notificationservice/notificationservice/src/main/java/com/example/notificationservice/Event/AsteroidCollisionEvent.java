package com.example.notificationservice.Event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsteroidCollisionEvent {
    private String asteroidName;
    private String closeApproachDate;
    private String missdistanceKilometer;
    private double estimatedDiameterAvgMeter;
}