package com.dophuong.question_service.repository;

import com.dophuong.question_service.entity.Question;
import com.dophuong.question_service.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionRepository {
    Question findById(Long id);
    Question save(Question question, Long courseId);

    boolean existsById(Long questionId);

    Page<Question> findAllByCourseId(Long courseId, Pageable pageable);

    void deleteById(Long questionId);

    int countByCourseId(Long courseId);

    int countByCourseIdAndDifficulty(Long courseId, Difficulty difficulty);

    List<Long> getQuestionsByLevel(Long courseId, String level);

    String getLevelByQuestionId(Long questionId);
}
