package com.tasktracker.service;

import com.tasktracker.dto.CompositeTaskResponse;
import com.tasktracker.dto.TaskRequest;
import com.tasktracker.dto.UserResponse;
import com.tasktracker.mapper.TaskMapper;
import com.tasktracker.model.Task;
import com.tasktracker.repo.TaskRepo;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final TaskRepo taskRepo;
    private final TaskMapper taskMapper;

    @Transactional(readOnly = true)
    public ResponseEntity<CompositeTaskResponse> findUserTasks(Long userId) {

        if (userId != null) {

            List<Task> tasks = taskRepo.findByUserId(userId);

            return ResponseEntity.status(HttpStatus.OK).body(CompositeTaskResponse.list(tasks,"There's your tasks"));

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

        if (!jwtUtil.validBearer(bearer)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CompositeTaskResponse.error("UNAUTHORIZED"));
        }

        Long userId = userService.userByBearer(bearer).getBody().getData().getId();

        if (userId != null && TaskRequest.isValid(taskRequest)) {

            Task task = new Task(
                    taskRequest.getTitle(),
                    taskRequest.getDescription(),
                    userService.findById(userId));

            taskRepo.save(task);
            log.info("Успешно сохранена задача: {}",task);

            return ResponseEntity.status(HttpStatus.OK).body(CompositeTaskResponse.success(task,"Your task successfully created"));
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(CompositeTaskResponse.error("Something wrong with your fields"));
    }


    @Transactional(readOnly = true)
    public ResponseEntity<CompositeTaskResponse> exactTask(String bearer, Long taskId) {

        if (!jwtUtil.validBearer(bearer)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CompositeTaskResponse.error("Unauthorized"));
        }

        String email = jwtUtil.extractUsername(bearer.substring(7));
        Optional<Task> task = taskRepo.findByTaskIdAndUserEmail(taskId,email);

        if (task.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(CompositeTaskResponse.success(task.get(),"Your task № "+taskId+" is"));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CompositeTaskResponse.error("NOT FOUND"));
    }

    @Transactional(readOnly = false)
    public ResponseEntity<CompositeTaskResponse> deleteTask(String bearer, Long taskId) {

        if (!jwtUtil.validBearer(bearer)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CompositeTaskResponse.error("Unauthorized"));
        }

        String email = jwtUtil.extractUsername(bearer.substring(7));
        Optional<Task> task = taskRepo.findByTaskIdAndUserEmail(taskId,email);

        if(task.isPresent()){

            taskRepo.delete(task.get());
            log.info("Удалена задача: {}",task.get());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(CompositeTaskResponse.success("Task №"+ taskId +" have been successfully deleted"));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CompositeTaskResponse.error("NOT FOUND"));
    }

    @Transactional(readOnly = false)
    public ResponseEntity<CompositeTaskResponse> updateTask(String bearer, TaskRequest request, Long taskId) {

        if (request == null && taskId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CompositeTaskResponse.error("BAD REQUEST"));
        }

        if (!jwtUtil.validBearer(bearer)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CompositeTaskResponse.error("Unauthorized"));
        }

        String email = jwtUtil.extractUsername(bearer.substring(7));
        Optional<Task> task = taskRepo.findByTaskIdAndUserEmail(taskId,email);

        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CompositeTaskResponse.error("NOT FOUND"));
        }

        Task taskToUpd = task.get();

        if (Boolean.TRUE.equals(taskToUpd.getCompleted())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(CompositeTaskResponse.error("This task is already completed and cannot be updated"));
        }

        taskMapper.updateTask(taskToUpd, request);

        if (Boolean.TRUE.equals(request.getCompleted())) {
            taskToUpd.setCompleted(true);
            taskToUpd.setCompletedAt(Instant.now());
        }

        taskRepo.saveAndFlush(taskToUpd);
        log.info("Обновлена задача: {}",taskToUpd);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CompositeTaskResponse.success(taskToUpd, "Your task had been successfully updated"));
    }

}