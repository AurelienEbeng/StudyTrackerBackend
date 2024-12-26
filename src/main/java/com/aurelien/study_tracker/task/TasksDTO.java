package com.aurelien.study_tracker.task;

import java.time.LocalDateTime;

public interface TasksDTO {
    Long getId();
    String getTitle();
    boolean getIsActive();
    LocalDateTime getDateCreated();
    Long getUserId();
}
