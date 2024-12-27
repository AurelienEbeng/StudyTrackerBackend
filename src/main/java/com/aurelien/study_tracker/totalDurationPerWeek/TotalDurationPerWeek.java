package com.aurelien.study_tracker.totalDurationPerWeek;

import com.aurelien.study_tracker.user.User;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDate;

@Entity
@Table(name = "total_duration_per_week")
public class TotalDurationPerWeek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Duration totalDuration;

    private LocalDate StartDate;

    private LocalDate EndDate;

    @ManyToOne
    private User user;
}
