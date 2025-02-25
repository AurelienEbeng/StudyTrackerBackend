package com.aurelien.study_tracker.session;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session,Long> {
    List<Session> findByTaskId(Long taskId);

    @Override
    Optional<Session> findById(Long id);
}
