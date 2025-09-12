package com.dophuong.lms.learning_management_system.enums;

public enum Gender {
    MALE("M"),
    FEMALE("F"),
    OTHER("O");

    private final String code;

    Gender(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
