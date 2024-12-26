package com.aurelien.study_tracker.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException() {
        super("Task Not Found");
    }
}
