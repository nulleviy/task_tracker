package com.tasktracker.service;

import com.tasktracker.messaging.SchedulerProducer;
import com.tasktracker.model.User;
import com.tasktracker.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class SchedulerService {
    private final UserRepo userRepo;
    private final SchedulerProducer producer;

    public SchedulerService(UserRepo userRepo, SchedulerProducer producer) {
        this.userRepo = userRepo;
        this.producer = producer;
    }

    @Scheduled(fixedDelayString = "PT20S")
    @Transactional(readOnly = true)
    @Async
    public void scheduledEmailMessage(){
        List<User> users = userRepo.findAll();
        users.forEach(producer::sendEmail);
    }

}
