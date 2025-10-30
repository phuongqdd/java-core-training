package com.dophuong.submission_service.repository.impl;

import com.dophuong.submission_service.dto.request.AnswerRequest;
import com.dophuong.submission_service.entity.SubmissionQuestionOption;
import com.dophuong.submission_service.repository.SubmissionQuestionOptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class SubmissionQuestionOptionRepositoryImpl implements SubmissionQuestionOptionRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Long findCorrectInQuestion(Long submissionQuestionId) {
        String sql = """
            SELECT submission_option_id
            FROM submission_question_option
            WHERE submission_question_id = :id AND is_correct = true
            """;

        Map<String, Object> params = Map.of("id", submissionQuestionId);

        return jdbcTemplate.queryForObject(sql, params, Long.class);
    }

    @Override
    public List<SubmissionQuestionOption> findBySubQuesId(Long submissionQuestionId) {
        String sql = """
                SELECT submission_option_id, is_chosen as chosen, is_correct as correct, option_id,
                    option_order, option_text,submission_question_id
                FROM submission_question_option
                WHERE submission_question_id = :id
                """;
        return jdbcTemplate.query(sql, Map.of("id", submissionQuestionId), new BeanPropertyRowMapper<>(SubmissionQuestionOption.class));
    }

    @Override
    public void updateOptionChosen(AnswerRequest answer) {
        String sql = """
                UPDATE submission_question_option
                SET is_chosen = 1
                WHERE submission_question_id = :idq AND submission_option_id = :ido
                """;
        jdbcTemplate.update(sql, Map.of("idq", answer.getSubmissionQuestionId(), "ido", answer.getChosenOptionId()));
    }
}
