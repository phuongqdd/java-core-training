package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.entity.Option;

import java.util.List;

public interface OptionRepository {
    List<Option> findByQuestionId(Long questionId);
    Option save(Option option, Long questionId);
}
