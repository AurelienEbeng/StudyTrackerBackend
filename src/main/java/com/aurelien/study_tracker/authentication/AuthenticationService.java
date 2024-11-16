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
import java.util.*;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordTokenRepository tokenRepository;

    public AuthenticationService(PasswordEncoder passwordEncoder, EmailService emailService) {
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }


    public String register(User request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistException();
        }
        var user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ADMIN);
        user.setDateJoined(LocalDateTime.now());
        user.setEnabled(true);
        userRepository.save(user);
        return "User Created Successfully";
    }


    public String registerUser(User request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
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
        return "User Created Successfully";
    }

    public String registerDemoUser(User request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
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
        return "Demo User Created Successfully";
    }


    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        authenticationManager.authenticate(authToken);
        User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new UserNotFoundException());

        if (!user.isEnabled()) {
            throw new AccountNotVerifiedException();
        }
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        return new AuthenticationResponse(jwt, user.getId(), user.getUsername());
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


    public String forgotPassord(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException());
        sendForgotPasswordEmail(user);
        return "Email sent";
    }

    public void sendForgotPasswordEmail(User user) {

        String resetLink = generateResetToken(user);

        Email email = new Email();
        email.setTo(user.getEmail());
        email.setSubject("Reset Password");
        email.setText("Hello \n\n" + "Please click on this link to Reset your Password :" + resetLink + ". \n\n" + "Regards \n" + "Aurelien");
        emailService.sendEmail(email);

    }

    public String generateResetToken(User user) {
        try{
            PasswordResetToken token  = tokenRepository.findByUserId(user.getId()).get();
            tokenRepository.delete(token);

        } catch (Exception e) {

        }

        UUID uuid = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expiryDateTime = currentDateTime.plusMinutes(30);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(expiryDateTime);
        resetToken.setUser(user);
        tokenRepository.save(resetToken);
        String endpointUrl = "http://localhost:8080/resetPassword";
        return endpointUrl + "?token=" + resetToken.getToken()+"&email="+user.getEmail();
    }


    public String resetPassword(PasswordResetRequest passwordResetRequest) {
        PasswordResetToken resetToken = tokenRepository.findByToken(passwordResetRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if(this.hasExpired(resetToken.getExpiryDateTime())) {
            throw new RuntimeException("Token has expired");
        }

        User user = userRepository.findByEmail(passwordResetRequest.getEmail()).orElseThrow(()-> new UserNotFoundException());
        user.setPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
        tokenRepository.delete(resetToken);
        return "Reset complete";
    }


    public boolean hasExpired(LocalDateTime expiryDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.isAfter(expiryDateTime);
    }
}