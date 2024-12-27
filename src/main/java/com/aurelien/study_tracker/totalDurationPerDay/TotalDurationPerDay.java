package com.aurelien.study_tracker.totalDurationPerDay;

import com.aurelien.study_tracker.user.User;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDate;

@Entity
@Table(name = "total_duration_per_day")
public class TotalDurationPerDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Duration totalDuration;

    private LocalDate dateCreated;

    @ManyToOne
    private User user;
}
