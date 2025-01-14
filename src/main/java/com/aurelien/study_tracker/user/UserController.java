package com.aurelien.study_tracker.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse>profile(@RequestParam Long userId){
        return ResponseEntity.ok(userService.profile(userId));
    }
}
