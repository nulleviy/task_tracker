package com.tasktracker.event;

import com.tasktracker.dto.EmailMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;

import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailEventHandler {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

    @EventListener
    @Async
    public void handleUserRegistered(UserRegisteredEvent event){

        log.info("Событие о регистрации в процессе для пользователя: {}",event.getUser().getEmail());
        EmailMessage welcomeEmail = EmailMessage.builder()
                                    .address(event.getUser().getEmail()).title("User Registration")
                                    .message("""
                                                Welcome to Task Tracker!
                                                You're successfully registered
                                             """)
                                    .build();

        kafkaTemplate.send("EMAIL_SENDING_TASKS", welcomeEmail);
        log.info("Отправлено сообщение о регистрации: {}",welcomeEmail);
    }

}
