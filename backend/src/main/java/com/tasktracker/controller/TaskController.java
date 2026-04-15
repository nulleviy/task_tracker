package com.tasktracker.controller;

import com.tasktracker.dto.CompositeTaskResponse;
import com.tasktracker.dto.TaskRequest;
import com.tasktracker.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Получение всех задач у аутентифицированного пользователя", description = "Выводит все задачи у пользователя")
    @GetMapping("/tasks")
    public ResponseEntity<CompositeTaskResponse> getTasks(@Parameter(description = "current user JWT token", example = "some jwt token")
                                                          @RequestHeader(value = "Authorization") String bearer){
        return taskService.tasksByBearer(bearer);
    }

    @Operation(summary = "Создание задачи", description = "Создаёт и сохраняет задачу в БД")
    @PostMapping("/tasks/create")
    public ResponseEntity<CompositeTaskResponse> createTask(@Parameter(description = "current user JWT token", example = "some jwt token")
                                                            @RequestHeader(value = "Authorization") String bearer,
                                                            @Parameter(description = "Реквест задачи в виде JSON")
                                                            @RequestBody TaskRequest request){
        return taskService.createTask(bearer, request);
    }

    @Operation(summary = "Получение конкретной задачи у пользователя", description = "Выводит конкретную задачу у пользователя")
    @GetMapping("/tasks/{id}")
    public ResponseEntity<CompositeTaskResponse> exactTask(@Parameter(description = "current user JWT token", example = "some jwt token")
                                                           @RequestHeader(value = "Authorization") String bearer,
                                                           @Parameter(description = "ID задачи")
                                                           @PathVariable Long id){
        return taskService.exactTask(bearer,id);
    }

    @Operation(summary = "Удаление задачи", description = "Удаляет задачу из БД")
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<CompositeTaskResponse> deleteTask(@Parameter(description = "current user JWT token",  example = "some jwt token")
                                                            @RequestHeader(value = "Authorization") String bearer,
                                                            @Parameter(description = "ID задачи")
                                                            @PathVariable Long id){
        return taskService.deleteTask(bearer,id);
    }

    @Operation(summary = "Обновление задачи", description = "Обновляет задачу в БД")
    @PatchMapping("/tasks/{id}")
    public ResponseEntity<CompositeTaskResponse> updateTask(@Parameter(description = "current user JWT token", example = "some jwt token")
                                                            @RequestHeader(value = "Authorization") String bearer,
                                                            @Parameter(description = "Реквест задачи в виде JSON")
                                                            @RequestBody TaskRequest request,
                                                            @Parameter(description = "ID задачи")
                                                            @PathVariable Long id){
        return taskService.updateTask(bearer,request,id);
    }



}
