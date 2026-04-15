package com.tasktracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskRequest {

    @Schema(example = "The Greatest Task")
    @NotBlank(message = "Task title is required")
    @Size(min = 2, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;

    @Schema(example = "10 ОТЖУМАНИ 10 БЕГИТ 10 ПРЭС")
    @NotBlank(message = "Task description is required")
    @Size(min = 2, max = 200, message = "Description must be between 1 and 200 characters")
    private String description;

    @Schema(example = "false")
    private Boolean completed = false;


    public static Boolean isValid(TaskRequest request) {
        return request != null
                && request.getTitle() != null
                && !request.getTitle().isBlank();
    }

}
