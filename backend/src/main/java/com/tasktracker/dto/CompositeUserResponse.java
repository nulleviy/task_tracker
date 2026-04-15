package com.tasktracker.dto;

import com.tasktracker.model.User;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompositeUserResponse {

    @Schema(description = "true")
    private boolean success;

    @Schema(description = "Works great")
    private String message;

    private UserResponse data;

    @Schema(example = "2026-01-31T21:40:32.836417500Z")
    private Instant timestamp;


    public static CompositeUserResponse success(UserResponse data, String message) {

        return CompositeUserResponse.builder()
               .success(true)
               .message(message)
               .data(data)
               .timestamp(Instant.now())
               .build();
    }

    public static CompositeUserResponse success(String message) {

        return CompositeUserResponse.builder()
               .success(true)
               .message(message)
               .timestamp(Instant.now())
               .build();
    }

    public static CompositeUserResponse success(User data, String message) {

        return CompositeUserResponse.builder()
               .success(true)
               .message(message)
               .data(new UserResponse(data.getId(),data.getEmail()))
               .timestamp(Instant.now())
               .build();
    }



    public static CompositeUserResponse error(String message) {

        return CompositeUserResponse.builder()
               .success(false)
               .message(message)
               .timestamp(Instant.now())
               .build();
    }

}
