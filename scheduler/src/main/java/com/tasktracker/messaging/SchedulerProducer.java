package com.tasktracker.messaging;

import com.tasktracker.dto.EmailMessage;
import com.tasktracker.model.User;
import com.tasktracker.service.EmailService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SchedulerProducer {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;
    private final EmailService emailService;

    private static final String TOPIC = "EMAIL_SENDING_TASKS";

    public SchedulerProducer(KafkaTemplate<String, EmailMessage> kafkaTemplate, EmailService emailService) {
        this.kafkaTemplate = kafkaTemplate;
        this.emailService = emailService;
    }

    public void sendEmail(User user){
        EmailMessage emailMessage = emailService.createEmailMessage(user);
        String key = emailMessage.getAddress();
        kafkaTemplate.send(TOPIC,key,emailMessage);
        System.out.println("Отправлено: "+emailMessage);
    }
}
