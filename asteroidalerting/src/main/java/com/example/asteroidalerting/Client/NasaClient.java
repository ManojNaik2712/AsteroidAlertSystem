package com.example.asteroidalerting.Client;

import com.example.asteroidalerting.DTO.Asteroid;
import com.example.asteroidalerting.DTO.NasaNeoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class NasaClient {

    @Value("{nasa.neo.api.url}")
    private String nasaNeoApiUrl;

    @Value("{nasa.api.key}")
    private String nasaApiKey;

    public List<Asteroid> getAesteroids(LocalDate fromDate, LocalDate toDate) {
        RestTemplate restTemplate=new RestTemplate();

        final NasaNeoResponse nasaNeoResponse=
                restTemplate.getForObject(geturl(fromDate,toDate), NasaNeoResponse.class);

       List<Asteroid> asteroidList=new ArrayList<>();
       if(nasaNeoResponse != null){
           asteroidList.addAll(nasaNeoResponse.getNearEarthObjects().values().stream().flatMap(List::stream).toList());
       }
       return asteroidList;
    }

    private String geturl(LocalDate fromDate, LocalDate toDate) {
        String apiUrl= UriComponentsBuilder.fromHttpUrl(nasaNeoApiUrl)
                .queryParam("start_date",fromDate)
                .queryParam("end_date",toDate)
                .queryParam("api_key",nasaApiKey)
                .build()
                .toUriString();
        return apiUrl;
    }

}
