package com.aurelien.study_tracker.session;

public class SessionCreateRequest {
    private String duration;
    private String comment;
    private Long taskId;
    private int year;
    private int month;
    private int day;

    public String getDuration() {
        return duration;
    }

    public String getComment() {
        return comment;
    }

    public Long getTaskId() {
        return taskId;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}
