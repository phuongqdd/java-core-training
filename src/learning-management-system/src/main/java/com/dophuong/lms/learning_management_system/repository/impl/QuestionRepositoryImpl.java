package com.dophuong.lms.learning_management_system.repository.impl;

import com.dophuong.lms.learning_management_system.entity.Question;
import com.dophuong.lms.learning_management_system.enums.Difficulty;
import com.dophuong.lms.learning_management_system.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
public class QuestionRepositoryImpl implements QuestionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public QuestionRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Question findById(Long id) {
        String sql = """
            SELECT question_id as id, content, image_url, explanation, difficulty, created_at, updated_at
            FROM question 
            WHERE question_id = :id
            """;
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    Map.of("id", id),
                    new BeanPropertyRowMapper<>(Question.class)
            );
        } catch (EmptyResultDataAccessException e) {
            return null; // hoặc throw custom exception QuestionNotFound
        }
    }

    @Override
    public Question save(Question question, Long courseId) {
        if(question.getCreatedAt() == null){
            question.setCreatedAt(LocalDateTime.now());
        }
        question.setUpdatedAt(LocalDateTime.now());
        if(question.getId() == null){
            String sql = """
                INSERT INTO question(course_id, content, difficulty, image_url, explanation, created_at, updated_at)
                VALUES (:courseId, :content, :difficulty, :imageUrl, :explanation, :createdAt, :updatedAt)
            """;

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("courseId", courseId)
                    .addValue("content", question.getContent())
                    .addValue("difficulty", question.getDifficulty().name())
                    .addValue("imageUrl", question.getImageUrl())
                    .addValue("explanation", question.getExplanation())
                    .addValue("createdAt", question.getCreatedAt())
                    .addValue("updatedAt", question.getUpdatedAt());

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(sql, params, keyHolder, new String[]{"question_id"});

            Long generatedId = keyHolder.getKey().longValue();
            question.setId(generatedId);
        }else{
            String sql = """
                    UPDATE question
                    SET content = :content, difficulty = :difficulty,
                        image_url = :imageUrl, explanation = :explanation, updated_at = :updatedAt
                    WHERE question_id = :id
                    """;
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("content", question.getContent())
                    .addValue("difficulty", question.getDifficulty().name())
                    .addValue("imageUrl", question.getImageUrl())
                    .addValue("explanation", question.getExplanation())
                    .addValue("updatedAt", question.getUpdatedAt())
                    .addValue("id", question.getId());
            jdbcTemplate.update(sql, params);

            String sql1 = "SELECT created_at FROM question WHERE question_id = :id";
            LocalDateTime time = jdbcTemplate.queryForObject(
                    sql1,
                    Map.of("id", question.getId()),
                    LocalDateTime.class
            );
            question.setCreatedAt(time);
        }

        return question;
    }

    @Override
    public boolean existsById(Long questionId) {
        String sql = "SELECT COUNT(*) FROM question WHERE question_id = :questionId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("questionId", questionId);

        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public List<Question> findAllByCourseId(Long courseId) {
        String sql = """
                SELECT question_id as id, content, image_url, explanation, difficulty, created_at, updated_at
                FROM question
                WHERE course_id = :id
                """;
        return jdbcTemplate.query(
                sql,
                Map.of("id", courseId),
                new BeanPropertyRowMapper<>(Question.class)
        );
    }

    @Override
    public void deleteById(Long questionId) {
        String sql = """
                DELETE FROM question
                WHERE question_id = :id
                """;
        jdbcTemplate.update(sql, Map.of("id", questionId));
    }

    @Override
    public int countByCourseId(Long courseId) {
        String sql = """
            SELECT COUNT(*)
            FROM question
            WHERE course_id = :id
            """;

        return jdbcTemplate.queryForObject(
                sql,
                Map.of("id", courseId),
                Integer.class
        );
    }

    @Override
    public int countByCourseIdAndDifficulty(Long courseId, Difficulty difficulty) {
        String sql = """
                SELECT COUNT(*)
                FROM question
                WHERE course_id = :id AND difficulty = :difficulty
                """;

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", courseId);
        source.addValue("difficulty", difficulty.name());
        return jdbcTemplate.queryForObject(sql, source, Integer.class);
    }
}
