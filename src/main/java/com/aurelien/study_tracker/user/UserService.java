package com.aurelien.study_tracker.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse profile(UserRequest userRequest){
        User user = userRepository.findById(userRequest.getId()).get();
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setId(user.getId());
        userResponse.setRole(user.getRole());
        userResponse.setUsername(user.getUsername());
        userResponse.setDateJoined(user.getDateJoined());
        return userResponse;
    }
}
