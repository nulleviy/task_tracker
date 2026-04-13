package com.tasktracker.service;

import com.tasktracker.dto.CompositeTaskResponse;
import com.tasktracker.dto.TaskRequest;
import com.tasktracker.dto.TaskResponse;
import com.tasktracker.dto.UserResponse;
import com.tasktracker.mapper.TaskMapper;
import com.tasktracker.model.Task;
import com.tasktracker.repo.TaskRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final TaskRepo taskRepo;
    private final TaskMapper taskMapper;
    public TaskService(JwtUtil jwtUtil, UserService userService, TaskRepo taskRepo, TaskMapper taskMapper) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.taskRepo = taskRepo;
        this.taskMapper = taskMapper;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<CompositeTaskResponse> findUserTasks(Long userId) {
        if (userId != null) {
            List<Task> tasks = taskRepo.findByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(CompositeTaskResponse.list(taskMapper.toResponse(tasks),"There's your tasks"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CompositeTaskResponse.error("NOT FOUND"));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<CompositeTaskResponse> tasksByBearer(String bearer) {
        if (jwtUtil.validBearer(bearer)) {
            UserResponse user = userService.userByBearer(bearer).getBody().getData();
            return findUserTasks(user.getId());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CompositeTaskResponse.error("UNAUTHORIZED"));
    }


    @Transactional(readOnly = false)
    public ResponseEntity<CompositeTaskResponse> createTask(String bearer, TaskRequest taskRequest) {
        if (jwtUtil.validBearer(bearer)) {
            Long userId = userService.userByBearer(bearer).getBody().getData().getId();
            if (userId != null && TaskRequest.isValid(taskRequest)) {
                Task task = new Task(
                        taskRequest.getTitle(),
                        taskRequest.getDescription(),
                        userService.findById(userId));
                taskRepo.save(task);
                return ResponseEntity.status(HttpStatus.OK).body(CompositeTaskResponse.success(taskMapper.toResponse(task),"Your task successfully created"));
            }
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(CompositeTaskResponse.error("Something wrong with your fields"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CompositeTaskResponse.error("UNAUTHORIZED"));
    }


    @Transactional(readOnly = true)
    public ResponseEntity<CompositeTaskResponse> exactTask(String bearer, Long taskId) {
        if (jwtUtil.validBearer(bearer)) {
            String email = jwtUtil.extractUsername(bearer.substring(7));
            Optional<Task> task = taskRepo.findByTaskIdAndUserEmail(taskId,email);
            if (task.isPresent()) {
                TaskResponse taskResponse = taskMapper.toResponse(task.get());
                return ResponseEntity.status(HttpStatus.OK).body(CompositeTaskResponse.success(taskResponse,"Your task № "+taskId+" is"));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CompositeTaskResponse.error("NOT FOUND"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CompositeTaskResponse.error("Unauthorized"));
    }

    @Transactional(readOnly = false)
    public ResponseEntity<CompositeTaskResponse> deleteTask(String bearer, Long taskId) {
        if (jwtUtil.validBearer(bearer)) {
            String email = jwtUtil.extractUsername(bearer.substring(7));
            Optional<Task> task = taskRepo.findByTaskIdAndUserEmail(taskId,email);
            if(task.isPresent()){
                taskRepo.delete(task.get());
                return ResponseEntity.status(HttpStatus.OK).body(CompositeTaskResponse.success("Task №"+ taskId +" have been successfully deleted"));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CompositeTaskResponse.error("NOT FOUND"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CompositeTaskResponse.error("Unauthorized"));
    }

    @Transactional(readOnly = false)
    public ResponseEntity<CompositeTaskResponse> updateTask(String bearer, TaskRequest request, Long taskId) {
        if (jwtUtil.validBearer(bearer)) {
            if (request != null && taskId != null) {
                String email = jwtUtil.extractUsername(bearer.substring(7));
                Optional<Task> task = taskRepo.findByTaskIdAndUserEmail(taskId,email);
                if (task.isPresent()) {
                    Task taskToUpd = task.get();
                    if (Boolean.TRUE.equals(taskToUpd.getCompleted())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(CompositeTaskResponse.error("This task is already completed and cannot be updated"));
                    }
                    taskMapper.updateTask(taskToUpd, request);
                    if (Boolean.TRUE.equals(request.getCompleted())) {
                        taskToUpd.setCompleted(true);
                        taskToUpd.setCompletedAt(Instant.now());
                    }
                    taskRepo.saveAndFlush(taskToUpd);
                    return ResponseEntity.status(HttpStatus.OK).body(CompositeTaskResponse.success(taskMapper.toResponse(taskToUpd), "Your task had been successfully updated"));
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CompositeTaskResponse.error("NOT FOUND"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CompositeTaskResponse.error("BAD REQUEST"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CompositeTaskResponse.error("Unauthorized"));
    }
}