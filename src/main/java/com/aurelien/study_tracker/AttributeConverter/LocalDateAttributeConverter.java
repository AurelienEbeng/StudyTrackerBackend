package com.aurelien.study_tracker.AttributeConverter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Date;
import java.time.LocalDate;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {


    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        if (localDate == null){
            return null;
        }
        return Date.valueOf(localDate);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        if(date == null){
            return null;
        }
        return date.toLocalDate();
    }
}
