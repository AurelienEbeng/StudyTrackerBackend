package com.aurelien.study_tracker.session;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody SessionCreateRequest request){
        try {
            sessionService.create(request);
            return ResponseEntity.ok("Session created successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getTaskSessions(@RequestParam Long taskId){
        try {
            var sessions = sessionService.getTaskSessions(taskId);
            return ResponseEntity.ok(sessions);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
