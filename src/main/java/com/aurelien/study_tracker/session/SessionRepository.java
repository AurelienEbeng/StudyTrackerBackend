package com.aurelien.study_tracker.session;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session,Long> {
    List<Session> findByTaskId(Long taskId);
}
