package com.aurelien.study_tracker.highlight;

import java.time.Duration;

public class HighlightDTO {
    private Duration totalDurationOverall;
    private Duration currentDayTotalDuration;
    private Duration lastWeekTotalDuration;
    private Duration currentWeekTotalDuration;


    public void setTotalDurationOverall(Duration totalDurationOverall) {
        this.totalDurationOverall = totalDurationOverall;
    }

    public void setCurrentDayTotalDuration(Duration currentDayTotalDuration) {
        this.currentDayTotalDuration = currentDayTotalDuration;
    }

    public void setLastWeekTotalDuration(Duration lastWeekTotalDuration) {
        this.lastWeekTotalDuration = lastWeekTotalDuration;
    }

    public void setCurrentWeekTotalDuration(Duration currentWeekTotalDuration) {
        this.currentWeekTotalDuration = currentWeekTotalDuration;
    }

    public String getTotalDurationOverall() {
        return totalDurationOverall.toString();
    }

    public String getCurrentDayTotalDuration() {
        return currentDayTotalDuration.toString();
    }

    public String getLastWeekTotalDuration() {
        return lastWeekTotalDuration.toString();
    }

    public String getCurrentWeekTotalDuration() {
        return currentWeekTotalDuration.toString();
    }
}
