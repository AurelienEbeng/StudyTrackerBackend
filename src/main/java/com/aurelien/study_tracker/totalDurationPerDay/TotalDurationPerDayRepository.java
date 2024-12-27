package com.aurelien.study_tracker.totalDurationPerDay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TotalDurationPerDayRepository extends JpaRepository<TotalDurationPerDay,Long> {
    TotalDurationPerDay findByUserIdAndDateCreated(Long userId, LocalDate dateCreated);
}
