package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.entity.Question;

import java.util.List;

public interface QuestionRepository {
    Question findById(Long id);
    Question save(Question question, Long courseId);

    boolean existsById(Long questionId);

    List<Question> findAllByCourseId(Long courseId);

    void deleteById(Long questionId);
}
