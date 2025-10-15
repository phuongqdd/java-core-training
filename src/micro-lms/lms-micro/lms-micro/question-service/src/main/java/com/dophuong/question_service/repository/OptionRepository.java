package com.dophuong.question_service.repository;


import com.dophuong.question_service.entity.Option;

import java.util.List;

public interface OptionRepository {
    List<Option> findByQuestionId(Long questionId);
    Option save(Option option, Long questionId);
    boolean existsById(int optionId);
    void deleteById(int optionId);

    void deleteByQuestionId(Long questionsId);

    Option update(Option option, Long questionId);
}
