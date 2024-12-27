package com.aurelien.study_tracker.totalDurationPerDay;

import com.aurelien.study_tracker.exception.UserNotFoundException;
import com.aurelien.study_tracker.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

@Service
public class TotalDurationPerDayService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TotalDurationPerDayRepository totalDurationPerDayRepository;

    public TotalDurationPerDay getTotalDurationPerDay(Long userId){
        var totalDurationPerDay = totalDurationPerDayRepository.findByUserIdAndDateCreated(userId,LocalDate.now());
        if(totalDurationPerDay==null){
            createTotalDurationPerDay(userId);
            getTotalDurationPerDay(userId);
        }
        return totalDurationPerDay;
    }

    public void createTotalDurationPerDay(Long userId){
        var totalDurationPerDay = new TotalDurationPerDay();
        totalDurationPerDay.setTotalDuration(Duration.parse("0s"));
        totalDurationPerDay.setDateCreated(LocalDate.now());
        var user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException());
        totalDurationPerDay.setUser(user);
    }
}
