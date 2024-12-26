package com.aurelien.study_tracker.exception;

public class TaskAlreadyExistException extends RuntimeException{
    public TaskAlreadyExistException() {
        super("Task Already exist");
    }
}