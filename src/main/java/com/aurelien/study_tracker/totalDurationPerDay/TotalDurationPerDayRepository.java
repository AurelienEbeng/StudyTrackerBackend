package com.aurelien.study_tracker.totalDurationPerDay;

import java.time.LocalDate;
import java.util.Optional;

public interface TotalDurationPerDayRepository<TotalDurationPerDay,Long> {
    Optional<TotalDurationPerDay> findByUserIdAndDateCreated(Long userId, LocalDate dateCreated);
}
