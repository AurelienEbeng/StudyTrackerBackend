package com.aurelien.study_tracker.task;

import com.aurelien.study_tracker.exception.TaskAlreadyExistException;
import com.aurelien.study_tracker.exception.TaskNotFoundException;
import com.aurelien.study_tracker.exception.UserNotFoundException;
import com.aurelien.study_tracker.user.User;
import com.aurelien.study_tracker.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        task.setState(request.getState());

        taskRepository.save(task);

    }

    public List<TaskDTO> getAll(Long userId){
        var tasks= taskRepository.findByUserId(userId);
        return toDTOs(tasks);
    }

    private List<TaskDTO> toDTOs(List<Task> tasks){
        List<TaskDTO> dtos = new ArrayList<>();
        for(Task t: tasks){
            TaskDTO dto = new TaskDTO();
            dto.setTitle(t.getTitle());
            dto.setId(t.getId());
            dto.setState(t.getState());
            dto.setUserId(t.getUser().getId());
            dto.setDateCreated(t.getDateCreated());
            dtos.add(dto);
        }

        return dtos;
    }

    public List<TaskDTO> getAllActive(Long userId){
        var tasks = taskRepository.findByUserIdAndState(userId,TaskState.ACTIVE);
        return toDTOs(tasks);
    }



}
