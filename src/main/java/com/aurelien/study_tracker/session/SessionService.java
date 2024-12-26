package com.aurelien.study_tracker.session;

import com.aurelien.study_tracker.exception.TaskNotFoundException;
import com.aurelien.study_tracker.task.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class SessionService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    SessionRepository sessionRepository;

    public void create(SessionCreateRequest request){
        var task = taskRepository.findById(request.getTaskId()).orElseThrow(()-> new TaskNotFoundException());
        Session session = new Session();
        session.setComment(request.getComment());
        session.setDuration(Duration.parse(request.getDuration()));
        session.setTask(task);
        session.setDateTimeCreated(LocalDateTime.now());
        sessionRepository.save(session);
    }
}
