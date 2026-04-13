package com.tasktracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompositeTaskResponse {
    @Schema(example = "true")

    private boolean success;
    @Schema(example = "HELLO EVERYTHING WORKS GREAT")

    private String message;
    private TaskResponse data;

    private List<TaskResponse> tasks;
    @Schema(example = "2026-01-31T21:40:32.836417500Z")

    private Instant timestamp;





    public static CompositeTaskResponse success(TaskResponse data, String message) {
        return CompositeTaskResponse.builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(Instant.now()) // автоматически ставим текущее время
                .build();
    }
    public static CompositeTaskResponse success(String message) {
        return CompositeTaskResponse.builder()
                .success(true)
                .message(message)
                .timestamp(Instant.now()) // автоматически ставим текущее время
                .build();
    }


    public static CompositeTaskResponse list(List<TaskResponse> data, String message){
        return CompositeTaskResponse.builder()
                .success(true)
                .message(message)
                .tasks(data)
                .timestamp(Instant.now())
                .build();
    }

    public static CompositeTaskResponse error(String message) {
        return CompositeTaskResponse.builder()
                .success(false)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }
}
