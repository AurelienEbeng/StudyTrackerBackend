package com.aurelien.study_tracker.totalDurationOverall;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TotalDurationOverallRepository extends JpaRepository<TotalDurationOverall, Long> {
    TotalDurationOverall findByUserId(Long userId);
}
