package com.aurelien.study_tracker.highlight;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/highlight")
public class HighlightController {
    private final HighlightService highlightService;

    public HighlightController(HighlightService highlightService) {
        this.highlightService = highlightService;
    }

    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam Long userId){
        try{
            return ResponseEntity.ok(highlightService.get(userId));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
