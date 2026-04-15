package com.tasktracker.messaging;

import com.tasktracker.dto.EmailMessage;
import com.tasktracker.model.User;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserProducer {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

    @Value("${my.spring.kafka.topic.name}")
    private String TOPIC;

    public void sendUserCreated(User user) {

        EmailMessage emailMessage = new EmailMessage(user.getEmail(),
                                    "Successfully Registered","Здарова братан у тебя все ахуительно");

        String key = emailMessage.getAddress();

        kafkaTemplate.send(TOPIC, key, emailMessage);
        log.info("Отправлено сообщение о регистрации: {}",emailMessage);
    }

    public UserProducer(KafkaTemplate<String, EmailMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

}
