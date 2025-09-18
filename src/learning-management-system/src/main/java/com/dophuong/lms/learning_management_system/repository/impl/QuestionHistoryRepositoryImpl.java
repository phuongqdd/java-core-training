package com.dophuong.lms.learning_management_system.repository.impl;

import com.dophuong.lms.learning_management_system.entity.QuestionHistory;
import com.dophuong.lms.learning_management_system.repository.QuestionHistoryRepository;
import com.dophuong.lms.learning_management_system.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class QuestionHistoryRepositoryImpl implements QuestionHistoryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<QuestionHistory> findByQuestionId(Long questionId) {
        String sql = "SELECT * FROM question_history WHERE question_id = :questionId";
        return jdbcTemplate.query(
                sql,
                Map.of("questionId", questionId),
                new BeanPropertyRowMapper<>(QuestionHistory.class)
        );
    }

    @Override
    public QuestionHistory save(QuestionHistory history) {
        String sql = """
            INSERT INTO question_history(user_id, question_id, action_type, time)
            VALUES (:userId, :questionId, :actionType, :time)
        """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", history.getUser().getId())
                .addValue("questionId", history.getQuestion().getId())
                .addValue("actionType", history.getActionType().name())
                .addValue("time", LocalDateTime.now());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long generatedId = keyHolder.getKey().longValue();
        history.setId(generatedId);
        return history;
    }
}
