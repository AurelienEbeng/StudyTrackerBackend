package com.aurelien.study_tracker.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByTitle(String email);
    Optional<Task> findById(Long id);

    @Query(value = "select t.id as id, t.date_created as dateCreated, t.is_active as isActive, t.title as title, t.user_id as userId\n" +
            "from tasks t where user_id = :userId", nativeQuery = true)
    Optional<List<TasksDTO>> findAllTasks(Long userId);
}