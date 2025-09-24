package com.dophuong.lms.learning_management_system.repository.impl;

import com.dophuong.lms.learning_management_system.entity.Course;
import com.dophuong.lms.learning_management_system.entity.Quiz;
import com.dophuong.lms.learning_management_system.enums.ActionType;
import com.dophuong.lms.learning_management_system.repository.QuizRepository;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class QuizRepositoryImpl implements QuizRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Quiz createQuiz(Long courseId, Quiz quiz) {
        String sql = """
                INSERT INTO quiz (course_id, title, description, open_time, close_time,
                            time_limit, attempts_allowed, pass_mark, allow_review,
                            is_published, created_at, updated_at, pct_rl, pct_un, pct_ap, pct_an, total)
                VALUES (:courseId, :title, :description, :openTime, :closeTime,
                        :timeLimit, :attemptsAllowed, :passMark, :allowReview,
                        :isPublished, :createdAt, :updatedAt, :pctRl, :pctUn, :pctAp, :pctAn, :total)
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
                .addValue("pctAn", quiz.getPctAn())
                .addValue("total", quiz.getTotal());

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
                SELECT q.quiz_id, q.title, q.description, q.is_published,
                        q.allow_review, q.attempts_allowed, q.open_time, q.close_time,
                        q.time_limit, q.pass_mark, q.total, q.pct_rl,
                        q.pct_un, q.pct_ap, q.pct_an,
                        c.course_id, c.name
                FROM quiz q JOIN course c
                    ON q.course_id = c.course_id
                WHERE quiz_id = :id
                """;
        Quiz quiz = jdbcTemplate.queryForObject(sql, Map.of("id", quizId),
                (rs, rowNum) -> {
                    Course course = Course.builder()
                            .id(rs.getLong("course_id"))
                            .name(rs.getString("name"))
                            .build();

                    return Quiz.builder()
                            .id(rs.getLong("quiz_id"))
                            .title(rs.getString("title"))
                            .description(rs.getString("description"))
                            .published(rs.getBoolean("is_published"))
                            .allowReview(rs.getBoolean("allow_review"))
                            .attemptsAllowed(rs.getInt("attempts_allowed"))
                            .openTime(rs.getTimestamp("open_time").toLocalDateTime())
                            .closeTime(rs.getTimestamp("close_time").toLocalDateTime())
                            .timeLimit(rs.getInt("time_limit"))
                            .passMark(rs.getFloat("pass_mark"))
                            .total(rs.getInt("total"))
                            .pctRl(rs.getInt("pct_rl"))
                            .pctUn(rs.getInt("pct_un"))
                            .pctAp(rs.getInt("pct_ap"))
                            .pctAn(rs.getInt("pct_an"))
                            .course(course) // gắn course vào quiz
                            .build();
                });

        return quiz;
    }

    @Override
    public boolean existsById(Long quizId) {
        String sql = """
                SELECT count(quiz_id)
                FROM quiz
                WHERE quiz_id = :id
                """;
        Integer cnt = jdbcTemplate.queryForObject(sql, Map.of("id", quizId), Integer.class);
        return cnt > 0 && cnt != null;
    }

    @Override
    public boolean existsQuestionInQuiz(Long quizId, Long questionId) {
        String sql = """
                SELECT count(quiz_question_id)
                FROM quiz_question
                WHERE quiz_id = :quizId AND question_id = :questionId
                """;
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("quizId", quizId)
                .addValue("questionId", questionId);

        Integer cnt = jdbcTemplate.queryForObject(sql, mapSqlParameterSource, Integer.class);
        return cnt > 0 && cnt != null;
    }

    @Override
    public void deleteQuestionInQuiz(Long courseId, Long quizId, Long questionId, Long userId) {
        // 1. Xóa khỏi quiz_question
        String sql1 = """
            DELETE FROM quiz_question
            WHERE quiz_id = :quizId
              AND question_id = :questionId
            """;
        jdbcTemplate.update(sql1, Map.of("quizId", quizId, "questionId", questionId));

        // 2. Cập nhật lại total
        String sql2 = """
               UPDATE quiz q
               LEFT JOIN (
                   SELECT qq.quiz_id,
                          COUNT(*) AS total,
                          SUM(CASE WHEN ques.difficulty = 'EASY' THEN 1 ELSE 0 END) AS rl,
                          SUM(CASE WHEN ques.difficulty = 'MEDIUM' THEN 1 ELSE 0 END) AS un,
                          SUM(CASE WHEN ques.difficulty = 'HARD' THEN 1 ELSE 0 END) AS ap,
                          SUM(CASE WHEN ques.difficulty = 'VERY_HARD' THEN 1 ELSE 0 END) AS an
                   FROM quiz_question qq
                   JOIN question ques ON qq.question_id = ques.question_id
                   WHERE qq.quiz_id = :id
                   GROUP BY qq.quiz_id
               ) t ON q.quiz_id = t.quiz_id
               SET q.total = t.total,
                   q.pct_rl = CASE WHEN t.total = 0 THEN 0 ELSE ROUND(t.rl * 100.0 / t.total) END,
                   q.pct_un = CASE WHEN t.total = 0 THEN 0 ELSE ROUND(t.un * 100.0 / t.total) END,
                   q.pct_ap = CASE WHEN t.total = 0 THEN 0 ELSE ROUND(t.ap * 100.0 / t.total) END,
                   q.pct_an = CASE WHEN t.total = 0 THEN 0 ELSE ROUND(t.an * 100.0 / t.total) END,
                   q.updated_at = :time
               WHERE q.quiz_id = :id
               """;

        MapSqlParameterSource updateQuiz = new MapSqlParameterSource()
                .addValue("id", quizId)
                .addValue("time", LocalDateTime.now());
        jdbcTemplate.update(sql2, updateQuiz);

        // 3. Lưu history
        String sql3 = """
                INSERT INTO quiz_history(action_type, action_time, quiz_id, user_id)
                VALUES(:type, :time, :quizId, :userId)
                """;
        MapSqlParameterSource insertHis = new MapSqlParameterSource()
                .addValue("type", ActionType.UPDATED.name())
                .addValue("time", LocalDateTime.now())
                .addValue("quizId", quizId)
                .addValue("userId", userId);
        jdbcTemplate.update(sql3, insertHis);
    }

    @Override
    public void addQuestionToQuiz(Long quizId, Long userId, Long questionId) {
        String sql = """
                CALL add_question_to_quiz(:quizId, :questionId, :userId)
                """;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                        .addValue("quizId", quizId)
                        .addValue("questionId", questionId)
                        .addValue("userId", userId);

        jdbcTemplate.update(sql, mapSqlParameterSource);
    }

    @Override
    public void updateQuiz(Long quizId, Long userId, Quiz quiz) {
        String sql = """
                UPDATE quiz
                SET title = :title,
                    description = :description,
                    open_time = :openTime,
                    close_time = :closeTime,
                    time_limit = :timeLimit,
                    attempts_allowed = :attemptsAllowed,
                    allow_review = :allowReview,
                    is_published = :isPublished,
                    updated_at = :updatedAt
                WHERE quiz_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", quizId)
                .addValue("title", quiz.getTitle())
                .addValue("description", quiz.getDescription())
                .addValue("openTime", quiz.getOpenTime())
                .addValue("closeTime", quiz.getCloseTime())
                .addValue("timeLimit", quiz.getTimeLimit())
                .addValue("attemptsAllowed", quiz.getAttemptsAllowed())
                .addValue("allowReview", quiz.isAllowReview())
                .addValue("isPublished", quiz.isPublished())
                .addValue("updatedAt", LocalDateTime.now());

        jdbcTemplate.update(sql, params);

        String sql1 = """
                INSERT INTO quiz_history(action_type, action_time, quiz_id, user_id)
                VALUES (:type, :time, :quizId, :userId)
                """;

        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("type", ActionType.UPDATED.name())
                .addValue("time", LocalDateTime.now())
                .addValue("quizId", quizId)
                .addValue("userId", userId);

        jdbcTemplate.update(sql1, source);
    }

    @Override
    public void deleteQuizById(Long quizId) {
        String sql = """
                CALL delete_quiz(:quizId)
                """;
        jdbcTemplate.update(sql, Map.of("quizId", quizId));
    }

    @Override
    public List<Quiz> findAllByCourseId(Long courseId) {
        String sql = """
            SELECT quiz_id as id, description, allow_review, attempts_allowed,
                    close_time, created_at, is_published as published, open_time, pass_mark,
                    pct_an, pct_ap, pct_rl, pct_un, time_limit, title,
                    updated_at, course_id , total
            FROM quiz
            WHERE course_id = :id
            """;

        return jdbcTemplate.query(
                sql,
                Map.of("id", courseId),
                new BeanPropertyRowMapper<>(Quiz.class)
        );
    }

    @Override
    public List<Quiz> findAllByCourseIdForStudent(Long courseId) {
        String sql = """
            SELECT quiz_id as id, description, allow_review, attempts_allowed,
                    close_time, created_at, is_published as published, open_time, pass_mark,
                    pct_an, pct_ap, pct_rl, pct_un, time_limit, title,
                    updated_at, course_id , total
            FROM quiz
            WHERE course_id = :id AND is_published = true
            """;

        return jdbcTemplate.query(
                sql,
                Map.of("id", courseId),
                new BeanPropertyRowMapper<>(Quiz.class)
        );
    }

    @Override
    public boolean existsByCourseIdAndQuizId(Long courseId, Long quizId) {
        String sql = """
                SELECT COUNT(*)
                FROM quiz
                WHERE quiz_id = :quizId, course_id = :courseId
                """;
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("quizId", quizId)
                .addValue("courseId", courseId);
        Integer cnt = jdbcTemplate.queryForObject(sql, source, Integer.class);
        return cnt != null && cnt > 0;
    }
}
