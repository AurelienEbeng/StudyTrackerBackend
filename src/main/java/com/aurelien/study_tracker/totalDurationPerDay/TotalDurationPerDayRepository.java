package com.aurelien.study_tracker.totalDurationPerDay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TotalDurationPerDayRepository extends JpaRepository<TotalDurationPerDay,Long> {
    TotalDurationPerDay findByUserIdAndDate(Long userId, LocalDate date);
}
