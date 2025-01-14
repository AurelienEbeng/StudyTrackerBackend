package com.aurelien.study_tracker.session;

import java.time.Duration;
import java.time.LocalDate;

public class SessionDTO {
    private Long id;

    private Duration duration;

    private String comment;

    private String date;

    private Long taskId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDuration() {
        return duration.toString();
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
