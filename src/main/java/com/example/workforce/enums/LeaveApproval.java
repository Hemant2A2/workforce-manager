package com.example.workforce.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum LeaveApproval {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String dbValue;
    LeaveApproval(String dbValue) { this.dbValue = dbValue; }
    public String getDbValue() { return dbValue; }

    private static final Map<String, LeaveApproval> BY_DB =
        Stream.of(values()).collect(Collectors.toMap(LeaveApproval::getDbValue, e -> e));

    public static LeaveApproval fromDb(String dbValue) {
        if (dbValue == null) return null;
        return BY_DB.get(dbValue);
    }
}
