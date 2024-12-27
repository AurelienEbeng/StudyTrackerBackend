package com.aurelien.study_tracker.highlight;

import com.aurelien.study_tracker.totalDurationOverall.TotalDurationOverall;
import com.aurelien.study_tracker.totalDurationOverall.TotalDurationOverallRepository;
import com.aurelien.study_tracker.totalDurationPerDay.TotalDurationPerDay;
import com.aurelien.study_tracker.totalDurationPerDay.TotalDurationPerDayRepository;
import com.aurelien.study_tracker.totalDurationPerWeek.TotalDurationPerWeek;
import com.aurelien.study_tracker.totalDurationPerWeek.TotalDurationPerWeekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
public class HighlightService {
    @Autowired
    TotalDurationOverallRepository totalDurationOverallRepository;

    @Autowired
    TotalDurationPerDayRepository totalDurationPerDayRepository;

    @Autowired
    TotalDurationPerWeekRepository totalDurationPerWeekRepository;

    public HighlightDTO get(Long userId){
        HighlightDTO dto = new HighlightDTO();

        setTotalDurationOverall(dto,userId);
        setCurrentDayTotalDuration(dto, userId);
        setLastWeekTotalDuration(dto, userId);
        setCurrentWeekTotalDuration(dto, userId);

        return dto;
    }

    private void setLastWeekTotalDuration(HighlightDTO dto, Long userId){
        LocalDate today = LocalDate.now();

        LocalDate currentWeekFirstDay = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate currentWeekLastDay = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        LocalDate lastWeekFirstDay = currentWeekFirstDay.minusDays(7);
        LocalDate lastWeekLastDay = currentWeekLastDay.minusDays(7);
        TotalDurationPerWeek totalDurationPerWeek =
                totalDurationPerWeekRepository.findByUserIdAndStartDateAndEndDate(userId,lastWeekFirstDay,lastWeekLastDay);
        if(totalDurationPerWeek==null){
            dto.setLastWeekTotalDuration(Duration.ZERO);
        }else {
            dto.setLastWeekTotalDuration(totalDurationPerWeek.getTotalDuration());
        }

    }

    private void setCurrentWeekTotalDuration(HighlightDTO dto, Long userId){
        LocalDate today = LocalDate.now();

        LocalDate currentWeekFirstDay = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate currentWeekLastDay = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        TotalDurationPerWeek totalDurationPerWeek =
                totalDurationPerWeekRepository.findByUserIdAndStartDateAndEndDate(userId, currentWeekFirstDay,currentWeekLastDay);
        if(totalDurationPerWeek==null){
            dto.setCurrentWeekTotalDuration(Duration.ZERO);
        }else{
            dto.setCurrentWeekTotalDuration(totalDurationPerWeek.getTotalDuration());
        }
    }

    private void setCurrentDayTotalDuration(HighlightDTO dto, Long userId){
        LocalDate today = LocalDate.now();
        TotalDurationPerDay totalDurationPerDay = totalDurationPerDayRepository.findByUserIdAndDate(userId, today);
        if(totalDurationPerDay==null){
            dto.setCurrentDayTotalDuration(Duration.ZERO);
        }else{
            dto.setCurrentDayTotalDuration(totalDurationPerDay.getTotalDuration());
        }
    }

    private void setTotalDurationOverall(HighlightDTO dto, Long userId){
        TotalDurationOverall totalDurationOverall = totalDurationOverallRepository.findByUserId(userId);
        if(totalDurationOverall==null){
            dto.setTotalDurationOverall(Duration.ZERO);
        }else{
            dto.setTotalDurationOverall(totalDurationOverall.getTotalDuration());
        }
    }
}
