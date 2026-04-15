package com.tasktracker.service;

import com.tasktracker.messaging.SchedulerProducer;
import com.tasktracker.model.User;
import com.tasktracker.repo.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {

    private final UserRepo userRepo;
    private final SchedulerProducer producer;

    @Scheduled(fixedDelayString = "PT20S")
    @Transactional(readOnly = true)
    @Async
    public void scheduledEmailMessage(){

        List<User> users = userRepo.findAll();
        users.forEach(producer::sendEmail);
    }

}
