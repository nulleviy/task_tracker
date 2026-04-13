package com.tasktracker.dto;

import lombok.*;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

}
