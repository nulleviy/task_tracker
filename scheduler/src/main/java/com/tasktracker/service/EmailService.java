package com.tasktracker.service;

import com.tasktracker.custom.email.EmailTypeMessage;
import com.tasktracker.dto.EmailMessage;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.List;

@Service
public class EmailService {

    public EmailMessage createEmailMessage(User user){

            List<Task> tasks = user.getTasks();

            if(!tasks.isEmpty()){

            ZoneId zone = ZoneId.systemDefault();
            Instant startOfDay = LocalDate.now(zone).atStartOfDay(zone).toInstant();
            Instant endOfDay = LocalDate.now(zone).plusDays(1).atStartOfDay(zone).toInstant();

            List<Task> incompleteTasks = tasks.stream().filter(task ->
                                         Boolean.FALSE.equals(task.getCompleted()))
                                         .toList();

            List<Task> todayCompleted = tasks.stream().filter(task ->
                                        task.getCompletedAt() != null &&
                                        !task.getCompletedAt().isBefore(startOfDay) &&
                                        task.getCompletedAt().isBefore(endOfDay))
                                        .toList();

            List<String> incompleteTitles = incompleteTasks.stream()
                                            .map(Task::getTitle)
                                            .limit(5)
                                            .toList();

            List<String> titles = todayCompleted.stream()
                                  .map(Task::getTitle)
                                  .limit(5)
                                  .toList();

            if(!incompleteTasks.isEmpty() && !todayCompleted.isEmpty()){
                return EmailTypeMessage.summary(user,titles,incompleteTitles);
            }
                if(!incompleteTasks.isEmpty()) {
                    return EmailTypeMessage.undoneTasks(user, incompleteTasks.size(), incompleteTitles);
                }

                if(!todayCompleted.isEmpty()){
                    return EmailTypeMessage.todayCompleted(user,titles,todayCompleted);
                }

            } return EmailTypeMessage.noTasks(user);
        }

}
