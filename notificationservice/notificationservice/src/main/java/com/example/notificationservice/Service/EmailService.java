package com.example.notificationservice.Service;
import com.example.notificationservice.Entity.Notification;
import com.example.notificationservice.Repository.NotificationRepository;
import com.example.notificationservice.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${email.service.from.email}")
    private String fromEmail;

    @Async
    public void sendAsteroidAlertEmail() {
        final String text = createEmailtext();
        if (text == null) {
            log.info("No asteroid to send a alert at {}", LocalDate.now());
            return;
        }
        final List<String> toemails = userRepository.findAllEmailAndNotificationEnabled();
        if (toemails.isEmpty()) {
            log.info("No users to send email to");
        }
        toemails.forEach(toemail -> sendEmail(toemail, text));
        log.info("Email sent to: #{} users", toemails.size());
    }

    private void sendEmail(String toemail, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toemail);
        message.setFrom(fromEmail);
        message.setSubject("Nasa asteroid collision event");
        message.setText(text);
        mailSender.send(message);
    }

    private String createEmailtext() {
        List<Notification> notificationList = notificationRepository.findByEmailSent(false);
        if (notificationList.isEmpty()) {
            return null;
        }

        StringBuilder emailtext = new StringBuilder();
        emailtext.append("Aseteroid alert: \n");
        emailtext.append("********************\n");

        notificationList.forEach(notification -> {
            emailtext.append("Asteroid Name: ").append(notification.getAsteroidName()).append("\n");
            emailtext.append("Close Approach Date: ").append(notification.getCloseApproachDate()).append("\n");
            emailtext.append("Estimated Diameter Avg Meters: ").append(notification.getEstimatedDiameterAvgMeters()).append("\n");
            emailtext.append("Miss Distance Kilometers: ").append(notification.getMissDistanceKilometers()).append("\n");
            emailtext.append("**********************\n");
            notification.setEmailSent(true);
            notificationRepository.save(notification);
        });
        return emailtext.toString();
    }
}
