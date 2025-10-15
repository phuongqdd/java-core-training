package com.dophuong.question_service.repository;

import com.dophuong.question_service.entity.Question;
import com.dophuong.question_service.enums.Difficulty;

import java.util.List;

public interface QuestionRepository {
    Question findById(Long id);
    Question save(Question question, Long courseId);

    boolean existsById(Long questionId);

    List<Question> findAllByCourseId(Long courseId);

    void deleteById(Long questionId);

    int countByCourseId(Long courseId);

    int countByCourseIdAndDifficulty(Long courseId, Difficulty difficulty);

    List<Long> getQuestionsByLevel(Long courseId, String level);

    String getLevelByQuestionId(Long questionId);
}
