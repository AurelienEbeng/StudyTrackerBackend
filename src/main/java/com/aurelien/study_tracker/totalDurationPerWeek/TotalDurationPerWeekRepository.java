package com.aurelien.study_tracker.totalDurationPerWeek;

import java.time.LocalDate;
import java.util.Optional;

public interface TotalDurationPerWeekRepository<TotalDurationPerWeek,Long> {
    Optional<TotalDurationPerWeek> findByUserIdAndStartDateGreaterThanAndEndDateLessThan
            (Long userId, LocalDate startDate, LocalDate endDate);
}
