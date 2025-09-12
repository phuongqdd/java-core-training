package com.dophuong.lms.learning_management_system.enums;

public enum Difficulty {
    EASY("E"),
    MEDIUM("M"),
    HARD("H"),
    VERY_HARD("VH"); // nếu muốn thêm mức khó thứ 4

    private final String code;

    Difficulty(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
