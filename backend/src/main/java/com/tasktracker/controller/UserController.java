package com.tasktracker.controller;

import com.tasktracker.dto.CompositeUserResponse;
import com.tasktracker.dto.UserRequest;
import com.tasktracker.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Запрос на регистрацию пользователя", description = "Регистрирует пользователя если все поля валидны и возвращает JWT Token")
    @PostMapping("/user")
    public ResponseEntity<CompositeUserResponse> userRegistration(@Parameter(description = "Запрос на регистрацию пользователя")
                                                                  @RequestBody UserRequest request){
        return userService.registration(request);
    }

    @Operation(summary = "Получение пользователя", description = "Выводит пользовател")
    @GetMapping("/user")
    public ResponseEntity<CompositeUserResponse> getUser(@Parameter(description = "JWT Token пользователя")
                                                         @RequestHeader(value = "Authorization", required = false) String bearer){
        return userService.userByBearer(bearer);
    }

    @Operation(summary = "Аутентифицирование пользователя", description = "если поля валидны то возвращает JWT Token для дальнейшей работы")
    @PostMapping("/auth/login")
    public ResponseEntity<CompositeUserResponse> userAuthorisation(@Parameter(description = "Запрос на аутентификацию пользователя")
                                                                   @RequestBody UserRequest request){
        return userService.authorization(request);
    }


}
