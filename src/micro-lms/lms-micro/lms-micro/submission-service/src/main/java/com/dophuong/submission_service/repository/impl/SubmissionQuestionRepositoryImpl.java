package com.dophuong.submission_service.repository.impl;

import com.dophuong.submission_service.dto.response.QuestionResponse;
import com.dophuong.submission_service.entity.SubmissionQuestion;
import com.dophuong.submission_service.repository.SubmissionQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SubmissionQuestionRepositoryImpl implements SubmissionQuestionRepository {
    @Autowired
    public NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<SubmissionQuestion> findBySubmissionId(Long submissionId) {
        String sql = """
                SELECT *, is_correct as correct
                FROM submission_question
                WHERE submission_id = :id
                """;
        return jdbcTemplate.query(sql, Map.of("id", submissionId), new BeanPropertyRowMapper<>(SubmissionQuestion.class));
    }

    @Override
    public void updateQuestionRs(Long submissionQuestionId, int check) {
        String sql = """
                UPDATE submission_question
                SET is_correct = :is
                WHERE submission_question_id = :id
                """;
        jdbcTemplate.update(sql, Map.of("is", check, "id", submissionQuestionId));
    }

}
