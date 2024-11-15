package com.aurelien.study_tracker.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;


    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public String sendEmail(Email email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(email.getTo());
            message.setSubject(email.getSubject());
            message.setText(email.getText());

            mailSender.send(message);
            return "success!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }




}
