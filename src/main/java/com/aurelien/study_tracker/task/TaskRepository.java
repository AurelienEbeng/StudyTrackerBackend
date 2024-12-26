package com.aurelien.study_tracker.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByTitle(String email);
    Optional<Task> findById(Long id);
    List<Task> findByUserId(Long userId);
}