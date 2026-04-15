package com.tasktracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "The Greatest Task")
    private String title;

    @Schema(example = "10 АНЖУМАНЕ 10 ПРЭС 10 БЕГАТ")
    private String description;

    @Schema(example = "4")
    private Long userId;

    @Schema(example = "false")
    private Boolean completed = false;

    @Schema(example = "2026-01-31T21:40:32.836417500Z")
    private Instant completedAt;

    @Schema(example = "2026-01-31T20:40:32.836417500Z")
    private Instant createdAt = Instant.now();

    public TaskResponse(Long id, String title, String description, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

}
