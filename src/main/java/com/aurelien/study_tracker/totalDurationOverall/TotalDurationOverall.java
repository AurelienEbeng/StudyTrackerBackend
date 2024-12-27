package com.aurelien.study_tracker.totalDurationOverall;

import com.aurelien.study_tracker.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.Duration;

@Entity
@Table(name = "total_duration_overall")
public class TotalDurationOverall {
    @OneToOne
    private User user;

    private Duration totalDuration;
}
