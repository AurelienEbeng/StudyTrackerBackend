package com.aurelien.study_tracker.totalDurationPerWeek;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TotalDurationPerWeekRepository extends JpaRepository<TotalDurationPerWeek,Long> {
    TotalDurationPerWeek findByUserIdAndStartDateAndEndDate
            (Long userId, LocalDate startDate, LocalDate endDate);
}
