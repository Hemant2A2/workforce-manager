package com.example.workforce.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LeaveApprovalConverter implements AttributeConverter<LeaveApproval, String> {

    @Override
    public String convertToDatabaseColumn(LeaveApproval attribute) {
        return attribute == null ? null : attribute.getDbValue();
    }

    @Override
    public LeaveApproval convertToEntityAttribute(String dbData) {
        return LeaveApproval.fromDb(dbData);
    }
}
