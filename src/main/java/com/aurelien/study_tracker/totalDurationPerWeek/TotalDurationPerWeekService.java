package com.aurelien.study_tracker.totalDurationPerWeek;

import com.aurelien.study_tracker.exception.UserNotFoundException;
import com.aurelien.study_tracker.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
public class TotalDurationPerWeekService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TotalDurationPerWeekRepository totalDurationPerWeekRepository;

    public TotalDurationPerWeek getTotalDurationPerWeek(Long userId){
        var totalDurationPerWeek = totalDurationPerWeekRepository.findByUserIdAndStartDateGreaterThanAndEndDateLessThan
                (userId, LocalDate.now(),LocalDate.now());

        if(totalDurationPerWeek==null){
            createTotalDurationPerWeek(userId);
            getTotalDurationPerWeek(userId);
        }

        return totalDurationPerWeek;
    }

    public void createTotalDurationPerWeek(Long userId){
        var totalDurationPerWeek = new TotalDurationPerWeek();
        var user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException());
        totalDurationPerWeek.setUser(user);
        totalDurationPerWeek.setTotalDuration(Duration.ZERO);
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        totalDurationPerWeek.setStartDate(firstDayOfWeek);
        totalDurationPerWeek.setEndDate(lastDayOfWeek);
        totalDurationPerWeekRepository.save(totalDurationPerWeek);
    }
}
