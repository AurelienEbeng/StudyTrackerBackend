package com.aurelien.study_tracker.task;

public class TaskUpdateRequest {
    private String title;
    private boolean isActive;
    private Long id;

    public String getTitle() {
        return title;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public Long getId() {
        return id;
    }

}
