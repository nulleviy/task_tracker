package com.tasktracker.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {

    private String address;
    private String title;
    private String message;

}
