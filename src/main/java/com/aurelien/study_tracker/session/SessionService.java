package com.aurelien.study_tracker.session;

import com.aurelien.study_tracker.exception.TaskNotFoundException;
import com.aurelien.study_tracker.exception.UserNotFoundException;
import com.aurelien.study_tracker.task.TaskRepository;
import com.aurelien.study_tracker.totalDurationOverall.TotalDurationOverall;
import com.aurelien.study_tracker.totalDurationOverall.TotalDurationOverallRepository;
import com.aurelien.study_tracker.totalDurationPerDay.TotalDurationPerDay;
import com.aurelien.study_tracker.totalDurationPerDay.TotalDurationPerDayRepository;
import com.aurelien.study_tracker.totalDurationPerWeek.TotalDurationPerWeek;
import com.aurelien.study_tracker.totalDurationPerWeek.TotalDurationPerWeekRepository;
import com.aurelien.study_tracker.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Service
public class SessionService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    TotalDurationOverallRepository totalDurationOverallRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TotalDurationPerDayRepository totalDurationPerDayRepository;

    @Autowired
    TotalDurationPerWeekRepository totalDurationPerWeekRepository;

    public void create(SessionCreateRequest request){
        var task = taskRepository.findById(request.getTaskId()).orElseThrow(()-> new TaskNotFoundException());
        Session session = new Session();
        session.setComment(request.getComment());
        session.setDuration(Duration.parse(request.getDuration()));
        session.setTask(task);
        session.setDateTimeCreated(LocalDateTime.now());
        sessionRepository.save(session);
    }

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

    public TotalDurationPerDay getTotalDurationPerDay(Long userId){
        var totalDurationPerDay = totalDurationPerDayRepository.findByUserIdAndDateCreated(userId, LocalDate.now());
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
