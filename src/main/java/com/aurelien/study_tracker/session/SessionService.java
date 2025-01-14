package com.aurelien.study_tracker.session;

import com.aurelien.study_tracker.exception.TaskNotFoundException;
import com.aurelien.study_tracker.task.Task;
import com.aurelien.study_tracker.task.TaskDTO;
import com.aurelien.study_tracker.task.TaskRepository;
import com.aurelien.study_tracker.totalDurationOverall.TotalDurationOverall;
import com.aurelien.study_tracker.totalDurationOverall.TotalDurationOverallRepository;
import com.aurelien.study_tracker.totalDurationPerDay.TotalDurationPerDay;
import com.aurelien.study_tracker.totalDurationPerDay.TotalDurationPerDayRepository;
import com.aurelien.study_tracker.totalDurationPerWeek.TotalDurationPerWeek;
import com.aurelien.study_tracker.totalDurationPerWeek.TotalDurationPerWeekRepository;
import com.aurelien.study_tracker.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class SessionService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    TotalDurationOverallRepository totalDurationOverallRepository;

    @Autowired
    TotalDurationPerDayRepository totalDurationPerDayRepository;

    @Autowired
    TotalDurationPerWeekRepository totalDurationPerWeekRepository;

    public void create(SessionCreateRequest request){
        var task = taskRepository.findById(request.getTaskId()).orElseThrow(()-> new TaskNotFoundException());
        Duration duration = Duration.parse(request.getDuration());
        LocalDate date = LocalDate.of(request.getYear(), request.getMonth(), request.getDay());

        Session session = new Session();
        session.setComment(request.getComment());
        session.setDuration(duration);
        session.setTask(task);
        session.setDate(date);
        sessionRepository.save(session);

        updateTotalDurationOverall(task.getUser(), duration);
        updateTotalDurationPerDay(task.getUser(),duration, date);
        updateTotalDurationPerWeek(task.getUser(),duration, date);
    }

    private void updateTotalDurationOverall(User user, Duration duration){
        var totalDurationOverall=totalDurationOverallRepository.findByUserId(user.getId());

        if(totalDurationOverall==null){
            createTotalDurationOverall(user, duration);
            return;
        }

        Duration totalDuration = duration.plus(totalDurationOverall.getTotalDuration());
        totalDurationOverall.setTotalDuration(totalDuration);
        totalDurationOverallRepository.save(totalDurationOverall);
    }

    private void createTotalDurationOverall(User user, Duration duration){
        TotalDurationOverall totalDurationOverall = new TotalDurationOverall();
        totalDurationOverall.setTotalDuration(duration);
        totalDurationOverall.setUser(user);
        totalDurationOverallRepository.save(totalDurationOverall);
    }

    private void updateTotalDurationPerDay(User user, Duration duration, LocalDate date){
        var totalDurationPerDay = totalDurationPerDayRepository.findByUserIdAndDate(user.getId(), date);
        if(totalDurationPerDay==null){
            createTotalDurationPerDay(user, duration, date);
            return;
        }

        Duration totalDuration = duration.plus(totalDurationPerDay.getTotalDuration());
        totalDurationPerDay.setTotalDuration(totalDuration);
        totalDurationPerDayRepository.save(totalDurationPerDay);
    }

    private void createTotalDurationPerDay(User user, Duration duration, LocalDate date){
        var totalDurationPerDay = new TotalDurationPerDay();
        totalDurationPerDay.setTotalDuration(duration);
        totalDurationPerDay.setDate(date);
        totalDurationPerDay.setUser(user);
        totalDurationPerDayRepository.save(totalDurationPerDay);
    }

    private void updateTotalDurationPerWeek(User user, Duration duration, LocalDate date){
        LocalDate firstDayOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate lastDayOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        var totalDurationPerWeek = totalDurationPerWeekRepository.findByUserIdAndStartDateAndEndDate
                (user.getId(), firstDayOfWeek, lastDayOfWeek);

        if(totalDurationPerWeek==null){
            createTotalDurationPerWeek(user, duration, date);
            return;
        }

        Duration totalDuration = duration.plus(totalDurationPerWeek.getTotalDuration());
        totalDurationPerWeek.setTotalDuration(totalDuration);
        totalDurationPerWeekRepository.save(totalDurationPerWeek);
    }

    public void createTotalDurationPerWeek(User user, Duration duration, LocalDate date){
        var totalDurationPerWeek = new TotalDurationPerWeek();
        totalDurationPerWeek.setUser(user);
        totalDurationPerWeek.setTotalDuration(duration);
        LocalDate firstDayOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate lastDayOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        totalDurationPerWeek.setStartDate(firstDayOfWeek);
        totalDurationPerWeek.setEndDate(lastDayOfWeek);
        totalDurationPerWeekRepository.save(totalDurationPerWeek);
    }

    public List<SessionDTO> getTaskSessions(Long taskId){
        var sessions = sessionRepository.findByTaskId(taskId);
        List<SessionDTO> dtos = new ArrayList<>();
        for(Session s: sessions){
            SessionDTO dto = new SessionDTO();
            dto.setComment(s.getComment());
            dto.setTaskId(s.getTask().getId());
            dto.setDuration(s.getDuration());
            dto.setId(s.getId());
            dto.setDate(s.getDate());
            dtos.add(dto);
            System.out.println(dto.getDate());
        }
        return dtos;
    }
}
