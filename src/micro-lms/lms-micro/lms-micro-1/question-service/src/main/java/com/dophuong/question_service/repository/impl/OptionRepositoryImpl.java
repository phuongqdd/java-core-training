package com.dophuong.question_service.repository.impl;

import com.dophuong.question_service.entity.Option;
import com.dophuong.question_service.repository.OptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class OptionRepositoryImpl implements OptionRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Option> findByQuestionId(Long questionId) {
        String sql = """
                SELECT *
                FROM `option`
                WHERE question_id = :questionId
                """;
        return jdbcTemplate.query(
                sql,
                Map.of("questionId", questionId),
                (rs, rowNum) -> Option.builder()
                        .id(rs.getInt("id"))
                        .content(rs.getString("content"))
                        .correct(rs.getBoolean("is_correct")) // ép thành boolean
                        .build()
        );
    }

    @Override
    public Option save(Option option, Long questionId) {
        String sql = """
                    INSERT INTO `option` (question_id, content, is_correct)
                    VALUES (:questionId, :content, :is_correct)
                    """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("questionId", questionId)
                .addValue("content", option.getContent())
                .addValue("is_correct", option.isCorrect());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
        option.setId(keyHolder.getKey().intValue());
        return option;
    }

    @Override
    public boolean existsById(int optionId) {
        String sql = """
                SELECT COUNT(*)
                FROM `option`
                WHERE id = :id
                """;
        Integer cnt = jdbcTemplate.queryForObject(sql,
                Map.of("id", optionId),
                Integer.class);
        return cnt != null && cnt > 0;
    }

    @Override
    public void deleteById(int optionId) {
        String sql = """
                DELETE FROM `option`
                WHERE id = :id
                """;
        jdbcTemplate.update(sql, Map.of("id", optionId));
    }

    @Override
    public void deleteByQuestionId(Long questionsId) {
        String sql = """
                DELETE FROM `option`
                WHERE question_id = :id
                """;
        jdbcTemplate.update(sql, Map.of("id", questionsId));
    }

    @Override
    public Option update(Option option, Long questionId) {
        if (option.getId() == null) {
            return save(option, questionId);
        } else {
            String sql = """
                UPDATE `option`
                SET content = :content, is_correct = :is_correct
                WHERE id = :id
                """;

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("content", option.getContent())
                    .addValue("is_correct", option.isCorrect())
                    .addValue("id", option.getId());

            jdbcTemplate.update(sql, params);
            return option;
        }
    }

}
