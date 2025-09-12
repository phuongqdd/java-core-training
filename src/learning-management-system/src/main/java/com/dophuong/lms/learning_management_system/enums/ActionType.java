package com.dophuong.lms.learning_management_system.enums;

public enum ActionType {
    CREATED("C"),
    UPDATED("U");

    private final String code;

    ActionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
