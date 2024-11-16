package com.aurelien.study_tracker.authentication;


import com.aurelien.study_tracker.config.JwtService;
import com.aurelien.study_tracker.email.Email;
import com.aurelien.study_tracker.email.EmailService;
import com.aurelien.study_tracker.exception.AccountNotVerifiedException;
import com.aurelien.study_tracker.exception.UserAlreadyExistException;
import com.aurelien.study_tracker.exception.UserNotFoundException;
import com.aurelien.study_tracker.user.Role;
import com.aurelien.study_tracker.user.User;
import com.aurelien.study_tracker.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;


    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public AuthenticationService(PasswordEncoder passwordEncoder, EmailService emailService) {
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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
        user.setEnabled(true);
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
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        sendVerificationEmail(user);
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
        user.setEnabled(true);
        userRepository.save(user);
        return  "Demo User Created Successfully";
    }




    public AuthenticationResponse login(AuthenticationRequest authenticationRequest){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()
        );
        authenticationManager.authenticate(authToken);
        User user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() ->  new UserNotFoundException());

        if (!user.isEnabled()) {
            throw new AccountNotVerifiedException();
        }
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        return new AuthenticationResponse(jwt, user.getId(),user.getUsername());
    }




    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("email", user.getEmail());
        extraClaims.put("role", user.getRole().name());
        return extraClaims;
    }


    public void verifyUser(VerifyUserRequest input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException();
        }
    }

    private void sendVerificationEmail(User user) {
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();

        Email email = new Email();
        email.setSubject("Account Verification");
        email.setTo(user.getEmail());
        email.setText(verificationCode);
        emailService.sendEmail(email);
    }
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}