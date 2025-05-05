package com.example.asteroidalerting.Service;

import com.example.asteroidalerting.Client.NasaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class AesteroidAlertService {

    @Autowired
    NasaClient nasaClient;

    public void alert() {

        //from and to date
        final LocalDate fromDate=LocalDate.now();
        final LocalDate toDate=LocalDate.now().plusDays(7);

        //call the Nasa api to get the aesteroid data
        nasaClient.getAesteroids(fromDate,toDate);

    }
}
