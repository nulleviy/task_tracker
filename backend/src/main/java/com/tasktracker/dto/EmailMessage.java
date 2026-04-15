package com.tasktracker.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmailMessage {

    private String address;
    private String title;
    private String message;

}
