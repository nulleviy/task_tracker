package com.tasktracker.messaging;

import com.tasktracker.dto.EmailMessage;
import com.tasktracker.model.User;
import com.tasktracker.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerProducer {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;
    private final EmailService emailService;

    private static final String TOPIC = "EMAIL_SENDING_TASKS";


    public void sendEmail(User user){

        EmailMessage emailMessage = emailService.createEmailMessage(user);
        String key = emailMessage.getAddress();
        kafkaTemplate.send(TOPIC,key,emailMessage);
        log.info("Отправлено сообщение: {}",emailMessage);
    }

}
