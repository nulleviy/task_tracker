package com.tasktracker.event;

import com.tasktracker.model.User;
import lombok.Getter;

@Getter
public class UserRegisteredEvent extends DomainEvent{
    private final User user;

    public UserRegisteredEvent(User user){
        this.user = user;
    }

}
