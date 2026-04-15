package com.tasktracker.dto;

import lombok.*;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class EmailMessage {

    private String address;
    private String title;
    private String message;

}
