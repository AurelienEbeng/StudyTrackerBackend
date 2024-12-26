package com.aurelien.study_tracker.task;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody TaskCreateRequest request){
        try {
            taskService.create(request);
            return ResponseEntity.ok("Task created successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody TaskUpdateRequest request){
        try {
            taskService.update(request);
            return ResponseEntity.ok("Task updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(@RequestParam Long userId){
        try {
            return ResponseEntity.ok(taskService.getAll(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllActive")
    public ResponseEntity<?> getAllActive(@RequestParam Long userId){
        try {
            return ResponseEntity.ok(taskService.getAllActive(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
