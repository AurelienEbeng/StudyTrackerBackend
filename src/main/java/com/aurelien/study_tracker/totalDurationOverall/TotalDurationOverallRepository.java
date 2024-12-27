package com.aurelien.study_tracker.totalDurationOverall;

import java.util.Optional;

public interface TotalDurationOverallRepository<TotalDurationOverall, Long>  {
    Optional<TotalDurationOverall> findByUserId(Long userId);
}
