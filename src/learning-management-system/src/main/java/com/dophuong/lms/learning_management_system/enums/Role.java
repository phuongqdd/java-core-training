package com.dophuong.lms.learning_management_system.enums;

public enum Role {
    STUDENT("STU"),
    INSTRUCTOR("INS"),
    ADMIN("ADM");

    private final String code;

    Role(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
