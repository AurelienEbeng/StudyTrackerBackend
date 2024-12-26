package com.aurelien.study_tracker.task;

public class TaskUpdateRequest {
    private String title;
    private TaskState state;
    private Long id;

    public String getTitle() {
        return title;
    }

    public TaskState getState() {
        return state;
    }

    public Long getId() {
        return id;
    }

}
