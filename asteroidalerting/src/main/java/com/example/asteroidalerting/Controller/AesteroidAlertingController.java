package com.example.asteroidalerting.Controller;

import com.example.asteroidalerting.Service.AesteroidAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AesteroidAlertingController {
    @Autowired
    AesteroidAlertService aesteroidAlertService;

    @PostMapping("/alert")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void alert(){
        aesteroidAlertService.alert();
    }
}
