package com.example.workforce.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Attendance {
    PRESENT("Present"),
    ABSENT("Absent"),
    LEAVE("Leave"),
    SICK("Sick");

    private final String dbValue;
    Attendance(String dbValue) { this.dbValue = dbValue; }
    public String getDbValue() { return dbValue; }

    private static final Map<String, Attendance> BY_DB =
        Stream.of(values()).collect(Collectors.toMap(Attendance::getDbValue, e -> e));

    public static Attendance fromDb(String dbValue) {
        if (dbValue == null) return null;
        return BY_DB.get(dbValue);
    }
}
