package com.dophuong.question_service.repository.impl;

import com.dophuong.question_service.entity.Question;
import com.dophuong.question_service.entity.QuestionHistory;
import com.dophuong.question_service.enums.ActionType;
import com.dophuong.question_service.repository.QuestionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QuestionHistoryRepositoryImpl implements QuestionHistoryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<QuestionHistory> findByQuestionId(Long questionId) {
        String sql = "SELECT * FROM question_history WHERE question_id = :questionId";
        List<QuestionHistory> questionHistories = jdbcTemplate.query(
                sql,
                Map.of("questionId", questionId),
                (rs, rowNum) -> {
                    QuestionHistory qh = new QuestionHistory();
                    qh.setId(rs.getLong("id"));
                    qh.setActionType(
                            ActionType.valueOf(rs.getString("action_type")) // gọn hơn, nhưng chú ý DB phải khớp enum
                    );
                    qh.setTime(rs.getTimestamp("time").toLocalDateTime());

                    Question question = Question.builder()
                            .id(rs.getLong("question_id"))
                            .build();
                    qh.setQuestion(question);


                    qh.setUserId(rs.getLong("user_id"));

                    return qh;
                }
        );

        log.warn("COn iên: {}", questionHistories.get(0));

        return questionHistories;
    }


    @Override
    public QuestionHistory save(QuestionHistory history) {
        String sql = """
            INSERT INTO question_history(user_id, question_id, action_type, time)
            VALUES (:userId, :questionId, :actionType, :time)
        """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", history.getUserId())
                .addValue("questionId", history.getQuestion().getId())
                .addValue("actionType", history.getActionType().name())
                .addValue("time", LocalDateTime.now());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long generatedId = keyHolder.getKey().longValue();
        history.setId(generatedId);
        return history;
    }

    @Override
    public void deleteByQuestionId(Long questionId) {
        String sql = """
                DELETE FROM question_history
                WHERE question_id = :id
                """;
        jdbcTemplate.update(sql, Map.of("id", questionId));
    }
}
