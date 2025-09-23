package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.entity.Quiz;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface QuizRepository{
    Quiz createQuiz(Long courseId, Quiz quiz);

    void createQuizRandomQuestion(Long quizId, Long userId, List<Integer> totalLevel);

    Quiz findById(Long quizId);

    boolean existsById(Long quizId);

    boolean existsQuestionInQuiz(Long quizId, Long questionId);

    void deleteQuestionInQuiz(Long courseId, Long quizId, Long questionId, Long userId);

    void addQuestionToQuiz(Long quizId, Long id, Long questionId);

    void updateQuiz(Long quizId, Long userId, Quiz quiz);

    void deleteQuizById(Long quizId);
}
