package com.dophuong.lms.learning_management_system.repository.impl;

import com.dophuong.lms.learning_management_system.entity.Option;
import com.dophuong.lms.learning_management_system.repository.OptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
        String sql = "SELECT * FROM option WHERE question_id = :questionId";
        return jdbcTemplate.query(
                sql,
                Map.of("questionId", questionId),
                new BeanPropertyRowMapper<>(Option.class)
        );
    }

    @Override
    public Option save(Option option, Long questionId) {
        log.warn("COn điên 2: " + option.isCorrect());
        if (option.getId() == null) {
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
        }

        return option;
    }

}
