package com.tasktracker.kafka;

import com.tasktracker.dto.CustomMailMessage;
import com.tasktracker.dto.EmailMessage;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final JavaMailSender mailSender;


    @PostConstruct
    public void init() {
        log.info("EmailConsumer создан и готов к работе!");
    }

    @KafkaListener(topics = "${my.spring.kafka.topic.name}",
                   groupId = "${my.spring.kafka.group}")
    public void listener(EmailMessage email){

        log.info("Received message = {}", email);
        CustomMailMessage customMailMessage = CustomMailMessage.buildMessage(email);

        mailSender.send(customMailMessage);
        log.info("Отправлено сообщение = {}", customMailMessage);
    }

}