package com.dophuong.quiz_service.util;

import java.util.function.Consumer;

public class StringUtil {

    /**
     * Cập nhật giá trị nếu chuỗi không null, không rỗng và loại bỏ khoảng trắng đầu/đuôi
     */
    public static void updateIfNotBlank(String value, Consumer<String> setter){
        if(value != null){
            String trimmed = value.trim();
            if(!trimmed.isEmpty()){
                setter.accept(trimmed);
            }
        }
    }

}
