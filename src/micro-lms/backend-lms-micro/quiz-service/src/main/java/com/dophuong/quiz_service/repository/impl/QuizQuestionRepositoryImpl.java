package com.dophuong.quiz_service.repository.impl;

import com.dophuong.quiz_service.dto.response.QuestionResponse;
import com.dophuong.quiz_service.repository.QuizQuestionRepository;
import com.dophuong.quiz_service.repository.feign.QuestionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QuizQuestionRepositoryImpl implements QuizQuestionRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private QuestionClient questionClient;

    @Override
    public List<QuestionResponse> findTestDetailsByQuizId(Long courseId, Long quizId) {
        String sql = """
                    SELECT question_id
                    FROM quiz_question
                    WHERE quiz_id = :id
                    """;
        List<Long> questionIds = jdbcTemplate.queryForList(sql, Map.of("id", quizId), Long.class);

        List<QuestionResponse> responses = new ArrayList<>();
        for(Long questionId : questionIds){
            QuestionResponse questionResponse = questionClient.getQuestion(courseId, questionId).getBody();
            responses.add(questionResponse);
        }
        return responses;
    }

    @Override
    public List<Long> findQuestionsExists(Long quizId) {
        String sql = """
                    SELECT question_id
                    FROM quiz_question
                    WHERE quiz_id = :id
                    """;
        return jdbcTemplate.queryForList(sql, Map.of("id", quizId), Long.class);
    }

    @Override
    public void save(Long quizId, Long questionId) {
        String sql = """
                INSERT INTO quiz_question(quiz_id, question_id)
                VALUES (:quizId, :questionId)
                """;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("quizId", quizId)
                .addValue("questionId", questionId);

        jdbcTemplate.update(sql, mapSqlParameterSource);
    }

    @Override
    public List<Long> findQuestionIdsByQuizId(Long quizId) {
        String sql = """
                SELECT question_id
                FROM quiz_question
                WHERE quiz_id = :id
                """;
        return jdbcTemplate.queryForList(sql, Map.of("id", quizId), Long.class);
    }
}
