package com.tasktracker.mapper;

import com.tasktracker.dto.TaskRequest;
import com.tasktracker.model.Task;

import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

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
