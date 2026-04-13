package com.tasktracker.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class EmailMessage {
    private String address;
    private String title;
    private String message;

    @Override
    public String toString() {
        return "EmailMessage{" +
                "address='" + address + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public EmailMessage(){}

    public EmailMessage(String address, String title, String message){
        this.address = address;
        this.title =  title;
        this.message = message;
    }

}
