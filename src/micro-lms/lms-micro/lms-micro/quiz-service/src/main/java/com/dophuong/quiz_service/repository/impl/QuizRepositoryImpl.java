package com.dophuong.quiz_service.repository.impl;

import com.dophuong.quiz_service.dto.response.QuestionResponse;
import com.dophuong.quiz_service.entity.Quiz;
import com.dophuong.quiz_service.enums.ActionType;
import com.dophuong.quiz_service.enums.Difficulty;
import com.dophuong.quiz_service.repository.QuizHistoryRepository;
import com.dophuong.quiz_service.repository.QuizQuestionRepository;
import com.dophuong.quiz_service.repository.QuizRepository;
import com.dophuong.quiz_service.repository.feign.QuestionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class QuizRepositoryImpl implements QuizRepository {
    @Autowired
    private QuizHistoryRepository quizHistoryRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuestionClient questionClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Prefix key chuẩn cho Redis
    private static final String CACHE_PREFIX = "courseQuestions:";

    @Override
    public Quiz createQuiz(Quiz quiz) {
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
                .addValue("courseId", quiz.getCourseId())
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
    public void createQuizRandomQuestion(Long quizId, List<Integer> totalLevel, Long courseId) {
        String []level = {Difficulty.EASY.name(), Difficulty.MEDIUM.name(), Difficulty.HARD.name(), Difficulty.VERY_HARD.name()};
        for(int i = 0; i < level.length; i++){
            addQuestionsForLevel(quizId, level[i], totalLevel.get(i), courseId);
        }

    }

    public void addQuestionsForLevel(Long quizId, String level, int p_total, Long courseId){
        List<Long> existingIds = quizQuestionRepository.findQuestionIdsByQuizId(quizId);
        String key = CACHE_PREFIX + courseId + ":" + level;
        List<Long> questions = new ArrayList<>();
        List<Long> cached = (List<Long>) redisTemplate.opsForValue().get(key);
        if(cached != null){
            questions = cached;
            log.warn("Đã gọi vào đây");
        }else{
            questions = questionClient.getQuestionsByLevel(courseId, level).getBody();
        }

        List<Long> availableQuestions = new java.util.ArrayList<>(questions.stream()
                .filter(q -> !existingIds.contains(q))
                .toList());

        if(availableQuestions.isEmpty()){
            return;
        }

        Collections.shuffle(availableQuestions);
        List<Long> selectedQuestions = availableQuestions.stream()
                .limit(p_total)
                .toList();

        for(Long id : selectedQuestions){
            quizQuestionRepository.save(quizId, id);
        }

    }

    @Override
    public Quiz findById(Long quizId) {
        String sql = """
            SELECT 
                quiz_id AS id,
                title,
                description,
                open_time,
                close_time,
                time_limit,
                total,
                attempts_allowed,
                allow_review,
                is_published,
                pct_rl,
                pct_un,
                pct_ap,
                pct_an,
                course_id,
                created_at,
                updated_at
            FROM quiz
            WHERE quiz_id = :id
        """;
        Map<String, Object> params = Map.of("id", quizId);

        return jdbcTemplate.queryForObject(
                sql,
                params,
                new BeanPropertyRowMapper<>(Quiz.class)
        );
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
                   JOIN question_service.question ques ON qq.question_id = ques.question_id
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
    public void addQuestionToQuiz(Long quizId, Long userId, Long questionId, Long courseId) {
        String insertQuizQuestion = """
            INSERT INTO quiz_question(quiz_id, question_id) VALUES (:quizId, :questionId)
        """;
        jdbcTemplate.update(insertQuizQuestion, Map.of("quizId", quizId, "questionId", questionId));

        List<Long> questionIds = quizQuestionRepository.findQuestionIdsByQuizId(quizId);
        int rl = 0, un = 0, ap = 0, an = 0;
        for(Long tmp : questionIds){
            log.warn("QuestionId: {}", tmp);
            String level = questionClient.getLevelByQuestionId(courseId, tmp).getBody();
            switch (level) {
                case "EASY": rl++; break;
                case "MEDIUM": un++; break;
                case "HARD": ap++; break;
                case "VERY_HARD": an++; break;
            }
        }

        int total = rl + un + ap + an;

        // Cập nhật phần trăm
        int pctRl = Math.round(rl * 100f / total);
        int pctUn = Math.round(un * 100f / total);
        int pctAp = Math.round(ap * 100f / total);
        int pctAn = Math.round(an * 100f / total);

        String sql1 = """
                UPDATE quiz
                SET total = :total, pct_rl = :rl, pct_un = :un, pct_ap = :ap, pct_an = :an, updated_at = NOW()
                WHERE quiz_id = :id
                """;

        MapSqlParameterSource source1 = new MapSqlParameterSource()
                .addValue("total", total)
                .addValue("rl", pctRl)
                .addValue("un", pctUn)
                .addValue("ap", pctAp)
                .addValue("an", pctAn)
                .addValue("id", quizId);

        // Lưu lại quiz
        jdbcTemplate.update(sql1, source1);

        String sql2 = """
                INSERT INTO quiz_history(action_type, action_time, quiz_id, user_id)
                VALUES('UPDATED', NOW(), :quizId, :userId)
                """;

        jdbcTemplate.update(sql2, Map.of("quizId", quizId, "userId", userId));
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
                WHERE quiz_id = :quizId AND course_id = :courseId
                """;
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("quizId", quizId)
                .addValue("courseId", courseId);
        Integer cnt = jdbcTemplate.queryForObject(sql, source, Integer.class);
        return cnt != null && cnt > 0;
    }

    @Override
    public int findAttemptsById(Long quizId) {
        String sql = """
            SELECT attempts_allowed
            FROM quiz
            WHERE quiz_id = :quizId
            """;

        Integer attempts = jdbcTemplate.queryForObject(sql, Map.of("quizId", quizId), Integer.class);
        return (attempts != null) ? attempts : 0;
    }

    public LocalDateTime findOpenTimeById(Long quizId) {
        String sql = """
                SELECT open_time
                FROM quiz
                WHERE quiz_id = :quizId
                """;

        return jdbcTemplate.queryForObject(
                sql,
                new MapSqlParameterSource("quizId", quizId),
                (rs, rowNum) -> rs.getTimestamp("open_time") != null
                        ? rs.getTimestamp("open_time").toLocalDateTime()
                        : null
        );
    }

    public LocalDateTime findCloseTimeById(Long quizId) {
        String sql = """
                SELECT close_time
                FROM quiz
                WHERE quiz_id = :quizId
                """;

        return jdbcTemplate.queryForObject(
                sql,
                new MapSqlParameterSource("quizId", quizId),
                (rs, rowNum) -> rs.getTimestamp("close_time") != null
                        ? rs.getTimestamp("close_time").toLocalDateTime()
                        : null
        );
    }

    @Override
    public List<QuestionResponse> findAllQuestionDetailByQuizId(Long courseId, Long quizId) {
        return quizQuestionRepository.findTestDetailsByQuizId(courseId, quizId);
    }

    @Override
    public List<Quiz> findAllQuizIds() {
        String sql = "SELECT quiz_id AS id, title, course_id,  is_published as published, open_time, close_time FROM quiz";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Quiz.class));
    }
}
