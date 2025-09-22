package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.entity.Quiz;

import java.util.List;

public interface QuizRepository{
    Quiz createQuiz(Long courseId, Quiz quiz);

    void createQuizRandomQuestion(Long quizId, Long userId, List<Integer> totalLevel);

    Quiz findById(Long quizId);
}
