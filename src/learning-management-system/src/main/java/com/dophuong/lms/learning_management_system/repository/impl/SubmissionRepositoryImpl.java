package com.dophuong.lms.learning_management_system.repository.impl;

import com.dophuong.lms.learning_management_system.dto.response.SubmissionQuestionOptionResponse;
import com.dophuong.lms.learning_management_system.dto.response.SubmissionQuestionResponse;
import com.dophuong.lms.learning_management_system.dto.response.SubmissionResponse;
import com.dophuong.lms.learning_management_system.entity.Option;
import com.dophuong.lms.learning_management_system.entity.Question;
import com.dophuong.lms.learning_management_system.repository.SubmissionRepository;
import com.dophuong.lms.learning_management_system.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SubmissionRepositoryImpl implements SubmissionRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public SubmissionResponse createSubmission(Long quizId, Long userId) {
        // Gọi procedure để insert submission
        String sql = "CALL create_submission(:quizId, :userId)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("quizId", quizId)
                .addValue("userId", userId);

        jdbcTemplate.update(sql, params);

        // Lấy submission mới nhất cho quiz đó
        String sql1 = """
                SELECT s.submission_id, q.quiz_id, q.title AS quizTitle,
                       s.attempt_no, s.status, q.time_limit
                FROM submission s
                         JOIN quiz q ON s.quiz_id = q.quiz_id
                WHERE q.quiz_id = :quizId
                ORDER BY s.started_at DESC
                LIMIT 1
                """;

        SubmissionResponse submissionResponse = jdbcTemplate.queryForObject(
                sql1,
                Map.of("quizId", quizId),
                new BeanPropertyRowMapper<>(SubmissionResponse.class)
        );

        String sql2 = """
                SELECT ss.submission_question_id, ss.question_text, ss.question_order,
                       sqo.submission_option_id, sqo.option_text, sqo.option_order, sqo.is_chosen AS chosen
                       FROM submission s
                       JOIN submission_question ss
                           ON s.submission_id = ss.submission_id
                       JOIN submission_question_option sqo
                           ON ss.submission_question_id = sqo.submission_question_id
                       JOIN quiz q
                           ON s.quiz_id = q.quiz_id
                       WHERE s.submission_id = (
                           SELECT submission_id
                           FROM submission
                           WHERE quiz_id = 2
                           ORDER BY started_at DESC
                           LIMIT 1
                       );
                """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql2, Map.of("quizId", quizId));

        Map<Long, SubmissionQuestionResponse> questionResponseMap = new HashMap<>();
        for (Map<String, Object> row : rows){
            Long questionId = (Long) row.get("submission_question_id");

            SubmissionQuestionResponse questionResponse = questionResponseMap.computeIfAbsent(questionId, aLong ->
                SubmissionQuestionResponse.builder()
                        .submissionQuestionId(questionId)
                        .questionText((String) row.get("question_text"))
                        .questionOrder((Integer) row.get("question_order"))
                        .options(new ArrayList<>())
                        .build()
            );

            SubmissionQuestionOptionResponse optionResponse = SubmissionQuestionOptionResponse.builder()
                    .submissionOptionId((Long) row.get("submission_option_id"))
                    .optionText((String) row.get("option_text"))
                    .optionOrder((Integer) row.get("option_order"))
                    .chosen((Boolean) row.get("chosen"))
                    .build();

            questionResponse.getOptions().add(optionResponse);
        }
        List<SubmissionQuestionResponse> list = new ArrayList<>(questionResponseMap.values());
        assert submissionResponse != null;
        submissionResponse.setQuestions(list);
        return submissionResponse;
    }

    @Override
    public int countQuizAttempts(Long userId, Long quizId) {
        String sql = """
            SELECT COUNT(*)
            FROM submission
            WHERE user_id = :userId AND quiz_id = :quizId
            """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("quizId", quizId);

        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return (count != null) ? count : 0;
    }
}
