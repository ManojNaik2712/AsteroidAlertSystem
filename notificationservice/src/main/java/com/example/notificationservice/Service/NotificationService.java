package com.example.notificationservice.Service;
import com.example.notificationservice.Entity.Notification;
import com.example.notificationservice.Event.AsteroidCollisionEvent;
import com.example.notificationservice.Repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    EmailService emailService;

    @KafkaListener(topics = "asteroid-alert", groupId = "asteroid-alert")
    public void alertEvent(AsteroidCollisionEvent notificationEvent) {
        log.info("Recieved event: {}", notificationEvent);

        //Building notification object
        final Notification notification = Notification.builder()
                .asteroidName(notificationEvent.getAsteroidName())
                .closeApproachDate(LocalDate.parse(notificationEvent.getCloseApproachDate()))
                .estimatedDiameterAvgMeters(notificationEvent.getEstimatedDiameterAvgMeter())
                .missDistanceKilometers(new BigDecimal(notificationEvent.getMissdistanceKilometer()))
                .emailSent(false)
                .build();

        final Notification savedNotification = notificationRepository.save(notification);
        log.info("notification saved: {}", savedNotification);
    }

    @Scheduled(fixedRate = 1000)
    public void sendAleteringEmail() {
        log.info("triggering the scheduled job to emial alerts");
        emailService.sendAsteroidAlertEmail();
    }
}
