package com.aurelien.study_tracker.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse profile(Long userId){
        User user = userRepository.findById(userId).get();
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setId(user.getId());
        userResponse.setRole(user.getRole());
        userResponse.setUsername(user.getUsername());
        userResponse.setDateJoined(user.getDateJoined().toString());
        return userResponse;
    }
}
