package com.aurelien.study_tracker.totalDurationOverall;

import com.aurelien.study_tracker.exception.UserNotFoundException;
import com.aurelien.study_tracker.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TotalDurationOverallService {
    @Autowired
    TotalDurationOverallRepository totalDurationOverallRepository;

    @Autowired
    UserRepository userRepository;


    public TotalDurationOverall getTotalDurationOverall(Long userId){
         var totalDurationOverall=totalDurationOverallRepository.findByUserId(userId);

         if(totalDurationOverall==null){
             createTotalDurationOverall(userId);
             getTotalDurationOverall(userId);
         }

         return totalDurationOverall;
    }

    public void createTotalDurationOverall(Long userId){
        TotalDurationOverall totalDurationOverall = new TotalDurationOverall();
        totalDurationOverall.setTotalDuration(Duration.parse("0S"));
        var user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException());
        totalDurationOverall.setUser(user);
        totalDurationOverallRepository.save(totalDurationOverall);

    }
}
