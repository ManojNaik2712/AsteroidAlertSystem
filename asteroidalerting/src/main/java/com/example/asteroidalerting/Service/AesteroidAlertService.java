package com.example.asteroidalerting.Service;

import com.example.asteroidalerting.Client.NasaClient;
import com.example.asteroidalerting.DTO.Asteroid;
import com.example.asteroidalerting.Events.AsteroidCollisonEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class AesteroidAlertService {

    @Autowired
    NasaClient nasaClient;

    @Autowired
    KafkaTemplate kafkaTemplate;

    public void alert() {

        //from and to date
        final LocalDate fromDate = LocalDate.now();
        final LocalDate toDate = LocalDate.now().plusDays(7);

        //call the Nasa api to get the aesteroid data
        final List<Asteroid> asteroidList = nasaClient.getAesteroids(fromDate, toDate);

        //if there is any hazardous asteroids, send an alert
        final List<Asteroid> dangerousAsteroids = asteroidList.stream()
                .filter(Asteroid::isPotentiallyHazardous)
                .toList();

        final List<AsteroidCollisonEvent> asteroidCollisonEventList =
                createEventListOfDangerousAsteroids(dangerousAsteroids);

        asteroidCollisonEventList.forEach(event-> {
            kafkaTemplate.send("asteroid-alert",event);
        });
    }

    private List<AsteroidCollisonEvent> createEventListOfDangerousAsteroids(List<Asteroid> dangerousAsteroids) {
        return dangerousAsteroids.stream()
                .map(asteroid -> {
                    return AsteroidCollisonEvent.builder()
                            .asteroidName(asteroid.getName())
                            .closeApproachDate(asteroid.getClosedApproachDataList().getFirst().getCloseApproachDate().toString())
                            .missdistanceKilometer(asteroid.getClosedApproachDataList().getFirst().getMissDistance().getKelometers())
                            .estimatedDiameterAvgMeter(asteroid.getEstimatedDiameter().getMeters().getMinDiameter() +
                                    asteroid.getEstimatedDiameter().getMeters().getMaxDiameter() / 2)
                            .build();
                })
                .toList();
    }
}
