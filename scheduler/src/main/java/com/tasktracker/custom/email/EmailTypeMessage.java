package com.tasktracker.custom.email;

import com.tasktracker.dto.EmailMessage;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailTypeMessage extends EmailMessage {

    public static EmailMessage  todayCompleted(User user, List<String> titles, List<Task> completedTasks){

        return new EmailMessage(user.getEmail(),"today completed"
                                +titles.toString(),"Братан ты сегодня сделал "
                                +completedTasks.size()+" задач");
    }

    public static EmailMessage summary(User user, List<String> todayCompletedTitle, List<String> incompleteTaskTitle){

        return new EmailMessage(user.getEmail(),"summary: +"
                                +todayCompletedTitle.toString()+" -",
                        "Ты не сделал "+incompleteTaskTitle.toString()+
                                "Ты сделал "+todayCompletedTitle);
    }

    public static EmailMessage undoneTasks(User user,int tasksCount,List<String> titles){

        return new EmailMessage(user.getEmail(),
                "У тебя осталось "+tasksCount+" задач",titles.toString());
    }

    public static EmailMessage noTasks(User user){

        return new EmailMessage(user.getEmail(),
                "Отсутствие задач","Другалек самое время создать себе задания а то че как лох");
    }


}
