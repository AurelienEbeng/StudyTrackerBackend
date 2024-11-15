package com.aurelien.study_tracker.authentication;


import com.aurelien.study_tracker.config.JwtService;
import com.aurelien.study_tracker.exception.UserAlreadyExistException;
import com.aurelien.study_tracker.user.Role;
import com.aurelien.study_tracker.user.User;
import com.aurelien.study_tracker.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;


    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public String register(User request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new UserAlreadyExistException();
        }
        var user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setDateJoined(LocalDateTime.now());
        userRepository.save(user);
        return  "User Created Successfully";
    }


    public String registerUser(User request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new UserAlreadyExistException();
        }
        var user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setDateJoined(LocalDateTime.now());
        userRepository.save(user);
        return  "User Created Successfully";
    }

    public String registerDemoUser(User request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new UserAlreadyExistException();
        }
        var user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.DEMO_USER);
        user.setDateJoined(LocalDateTime.now());
        userRepository.save(user);
        return  "Demo User Created Successfully";
    }




    public AuthenticationResponse login(AuthenticationRequest authenticationRequest){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()
        );
        authenticationManager.authenticate(authToken);
        User user = userRepository.findByEmail(authenticationRequest.getEmail()).get();
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        return new AuthenticationResponse(jwt, user.getId(),user.getUsername());
    }




    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("email", user.getEmail());
        extraClaims.put("role", user.getRole().name());
        return extraClaims;
    }
}