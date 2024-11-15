package com.aurelien.study_tracker.exception;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException() {
        super("User Already exist");
    }
}