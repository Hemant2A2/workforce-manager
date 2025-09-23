package com.example.workforce.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Others");

    private final String dbValue;

    Gender(String dbValue) { this.dbValue = dbValue; }

    @JsonValue
    public String getDbValue() { return dbValue; }

    private static final Map<String, Gender> BY_DB =
        Stream.of(values()).collect(Collectors.toMap(Gender::getDbValue, e -> e));

    @JsonCreator
    public static Gender fromDb(String dbValue) {
        if (dbValue == null) return null;
        return BY_DB.get(dbValue);
    }
}
