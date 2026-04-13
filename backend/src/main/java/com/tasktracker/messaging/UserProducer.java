package com.tasktracker.messaging;

import com.tasktracker.dto.EmailMessage;
import com.tasktracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserProducer {

    @Autowired
    private KafkaTemplate<String, EmailMessage> kafkaTemplate;


    private static final String TOPIC = "EMAIL_SENDING_TASKS";

    public void sendUserCreated(User user) {
        EmailMessage emailMessage = new EmailMessage(user.getEmail(),"Successfully Registered","Здарова братан у тебя все ахуительно");

        String key = emailMessage.getAddress();
        kafkaTemplate.send(TOPIC,key, emailMessage);

        System.out.println("Отправлено: "+emailMessage);
    }
}
