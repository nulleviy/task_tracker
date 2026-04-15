package com.tasktracker.dto;

import org.springframework.mail.SimpleMailMessage;

public class CustomMailMessage extends SimpleMailMessage {

    public static CustomMailMessage buildMessage(EmailMessage emailMessage) {

        CustomMailMessage customMailMessage = new CustomMailMessage();

        customMailMessage.setTo(emailMessage.getAddress());
        customMailMessage.setSubject(emailMessage.getTitle());
        customMailMessage.setText(customMailMessage.formatBody(emailMessage.getMessage()));
        customMailMessage.setFrom("nulleviy@boss.ru");

        return customMailMessage;
    }
    private String formatBody(String originalBody) {

        return """
               🎯 Task Tracker Email Notification
               
               %s
               
               ---
               С уважением,
               Команда Task Tracker
               """.formatted(originalBody);
    }

}
