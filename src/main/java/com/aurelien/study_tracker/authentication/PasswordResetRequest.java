package com.aurelien.study_tracker.authentication;

public class PasswordResetRequest {

    private String email;
    private String password;
    private String confirmPassword;
    private String token;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getToken(){
        return  token;
    }
}
