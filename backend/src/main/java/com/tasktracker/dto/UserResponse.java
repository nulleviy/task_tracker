package com.tasktracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "newТaaaaa1@mail.ru")
    private String email;

}
