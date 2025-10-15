package com.dophuong.quiz_service.repository;


import com.dophuong.quiz_service.entity.Quiz;

import java.time.LocalDateTime;
import java.util.List;

public interface QuizRepository{
    Quiz createQuiz(Long courseId, Quiz quiz);

    void createQuizRandomQuestion(Long quizId, List<Integer> totalLevel, Long courseId);

    Quiz findById(Long quizId);

    boolean existsById(Long quizId);

    boolean existsQuestionInQuiz(Long quizId, Long questionId);

    void deleteQuestionInQuiz(Long courseId, Long quizId, Long questionId, Long userId);

    void addQuestionToQuiz(Long quizId, Long id, Long questionId, Long courseId);

    void updateQuiz(Long quizId, Long userId, Quiz quiz);

    void deleteQuizById(Long quizId);

    List<Quiz> findAllByCourseId(Long courseId);

    List<Quiz> findAllByCourseIdForStudent(Long courseId);

    boolean existsByCourseIdAndQuizId(Long courseId, Long quizId);

    int findAttemptsById(Long quizId);

    LocalDateTime findOpenTimeById(Long quizId);

    LocalDateTime findCloseTimeById(Long quizId);
}
