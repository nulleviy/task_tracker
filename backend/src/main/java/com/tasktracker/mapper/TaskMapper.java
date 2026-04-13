package com.tasktracker.mapper;

import com.tasktracker.dto.TaskRequest;
import com.tasktracker.dto.TaskResponse;
import com.tasktracker.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class TaskMapper {
    public List<TaskResponse> toResponse(List<Task> tasks) {
        return tasks.stream().map(t -> new TaskResponse(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getCompleted(),
                t.getCompletedAt(),
                t.getCreatedAt(),
                t.getUser().getId()
        )).collect(Collectors.toList());
    }

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getUser().getId());
    }

    public void updateTask(Task task, TaskRequest request){
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if(request!=null){
            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                task.setTitle(request.getTitle());
            }
            if (request.getDescription() != null) {
                task.setDescription(request.getDescription());
            }
        }
    }

}
