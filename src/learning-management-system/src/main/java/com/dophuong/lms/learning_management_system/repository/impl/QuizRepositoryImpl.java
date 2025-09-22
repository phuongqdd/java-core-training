package com.dophuong.lms.learning_management_system.repository.impl;

import com.dophuong.lms.learning_management_system.entity.Quiz;
import com.dophuong.lms.learning_management_system.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuizRepositoryImpl implements QuizRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Quiz createQuiz(Long courseId, Quiz quiz) {
        String sql = """
                INSERT INTO quiz (course_id, title, description, open_time, close_time,
                            time_limit, attempts_allowed, pass_mark, allow_review,
                            is_published, created_at, updated_at, pct_rl, pct_un, pct_ap, pct_an)
                VALUES (:courseId, :title, :description, :openTime, :closeTime,
                        :timeLimit, :attemptsAllowed, :passMark, :allowReview,
                        :isPublished, :createdAt, :updatedAt, :pctRl, :pctUn, :pctAp, :pctAn)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("courseId", courseId)
                .addValue("title", quiz.getTitle())
                .addValue("description", quiz.getDescription())
                .addValue("openTime", quiz.getOpenTime())
                .addValue("closeTime", quiz.getCloseTime())
                .addValue("timeLimit", quiz.getTimeLimit())
                .addValue("attemptsAllowed", quiz.getAttemptsAllowed())
                .addValue("passMark", quiz.getPassMark())
                .addValue("allowReview", quiz.isAllowReview())
                .addValue("isPublished", quiz.isPublished())
                .addValue("createdAt", LocalDateTime.now())
                .addValue("updatedAt", LocalDateTime.now())
                .addValue("pctRl", quiz.getPctRl())
                .addValue("pctUn", quiz.getPctUn())
                .addValue("pctAp", quiz.getPctAp())
                .addValue("pctAn", quiz.getPctAn());

        jdbcTemplate.update(sql, params, keyHolder, new String[]{"quiz_id"});
        Long id = keyHolder.getKey().longValue();
        quiz.setId(id);
        return quiz;
    }

    @Override
    public void createQuizRandomQuestion(Long quizId, Long userId, List<Integer> totalLevel) {
        String sql = "CALL create_quiz_random(:quizId, :userId, :totalRl, :totalUn, :totalAp, :totalAn)";

        Map<String, Object> params = new HashMap<>();
        params.put("quizId", quizId);
        params.put("userId", userId);
        params.put("totalRl", totalLevel.get(0));
        params.put("totalUn", totalLevel.get(1));
        params.put("totalAp", totalLevel.get(2));
        params.put("totalAn", totalLevel.get(3));

        jdbcTemplate.update(sql, params);
    }

    @Override
    public Quiz findById(Long quizId) {
        String sql = """
                SELECT quiz_id, title, description, is_published,
                        allow_review, attempts_allowed, open_time, close_time,
                        time_limit, pass_mark, total, pct_rl,
                        pct_un, pct_ap, pct_an, course_id
                FROM quiz
                WHERE quiz_id = :id
                """;
        return jdbcTemplate.queryForObject(sql, Map.of("id", quizId), new BeanPropertyRowMapper<>());
    }
}
