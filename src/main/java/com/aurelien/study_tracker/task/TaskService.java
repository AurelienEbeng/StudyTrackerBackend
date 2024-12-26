package com.aurelien.study_tracker.task;

import com.aurelien.study_tracker.exception.TaskAlreadyExistException;
import com.aurelien.study_tracker.exception.TaskNotFoundException;
import com.aurelien.study_tracker.exception.UserNotFoundException;
import com.aurelien.study_tracker.user.User;
import com.aurelien.study_tracker.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    public void create(TaskCreateRequest request){
        if (taskRepository.findByTitle(request.getTitle()).isPresent()){
            throw new TaskAlreadyExistException();
        }

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException());

        var task = new Task();
        task.setDateCreated(LocalDateTime.now());
        task.setTitle(request.getTitle());
        task.setUser(user);
        taskRepository.save(task);
    }

    public void update(TaskUpdateRequest request){

        var task = taskRepository.findById(request.getId()).orElseThrow(()-> new TaskNotFoundException());

        task.setTitle(request.getTitle());
        task.setActive(request.getIsActive());
        taskRepository.save(task);

    }
}
