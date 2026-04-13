package com.tasktracker.kafka;

import com.tasktracker.dto.CustomMailMessage;
import com.tasktracker.dto.EmailMessage;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailConsumer {

    @PostConstruct
    public void init() {
        log.info("📢 EmailConsumer создан и готов к работе!");
        System.out.println("ХУЙХУХЙХУЙХУЙХ");
    }
    private final JavaMailSender mailSender;
    public EmailConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "EMAIL_SENDING_TASKS", groupId = "email-sender-group")
    public void listener(EmailMessage email){
        log.info("Received message = {}", email);
        CustomMailMessage customMailMessage = CustomMailMessage.buildMessage(email);
        mailSender.send(customMailMessage);
    }
}