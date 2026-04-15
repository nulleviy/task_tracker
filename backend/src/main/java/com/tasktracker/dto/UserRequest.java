package com.tasktracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {

    @Schema(example = "something@mail.ru")
    private String email;

    @Schema(example = "pasxalko1337")
    private String password;

}
