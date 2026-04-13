package com.tasktracker.repo;

import com.tasktracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends JpaRepository<Task,Long> {
    List<Task> findByUserId(Long userId);

    @Query("SELECT t FROM Task t JOIN FETCH t.user u WHERE t.id = :taskId AND u.email = :email")
    Optional<Task> findByTaskIdAndUserEmail(@Param("taskId") Long taskId,
                                            @Param("email") String email);
}
