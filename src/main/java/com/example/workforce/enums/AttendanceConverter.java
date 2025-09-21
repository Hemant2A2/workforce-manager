package com.example.workforce.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AttendanceConverter implements AttributeConverter<Attendance, String> {

    @Override
    public String convertToDatabaseColumn(Attendance attribute) {
        return attribute == null ? null : attribute.getDbValue();
    }

    @Override
    public Attendance convertToEntityAttribute(String dbData) {
        return Attendance.fromDb(dbData);
    }
}
