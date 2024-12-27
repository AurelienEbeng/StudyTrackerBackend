package com.aurelien.study_tracker.totalDurationOverall;

import com.aurelien.study_tracker.user.User;
import jakarta.persistence.*;

import java.time.Duration;

@Entity
@Table(name = "total_duration_overall")
public class TotalDurationOverall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    private Duration totalDuration;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Duration getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
    }
}
