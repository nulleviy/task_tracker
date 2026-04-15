package com.tasktracker.event;

import com.tasktracker.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegisteredEvent extends DomainEvent{
    private final User user;
}