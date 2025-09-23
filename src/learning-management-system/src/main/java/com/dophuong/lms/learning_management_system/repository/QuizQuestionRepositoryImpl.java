package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.dto.response.OptionResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuestionResponse;
import com.dophuong.lms.learning_management_system.enums.Difficulty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuizQuestionRepositoryImpl implements QuizQuestionRepository{

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<QuestionResponse> findTestDetailsByQuizId(Long quizId) {
        String sql = """
                    SELECT q.question_id, q.content, q.image_url, q.difficulty, q.explanation,
                           o.id AS option_id, o.content AS option_content, o.is_correct
                    FROM quiz_question qq
                    JOIN question q ON qq.question_id = q.question_id
                    JOIN option o ON q.question_id = o.question_id
                    WHERE qq.quiz_id = :id
                    """;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, Map.of("id", quizId));
        Map<Long, QuestionResponse> questionResponseMap = new HashMap<>();
        for(Map<String, Object> row: rows){
            Long questionId = ((Number) row.get("question_id")).longValue();

            QuestionResponse question = questionResponseMap.computeIfAbsent(questionId, aLong ->
                    QuestionResponse.builder()
                            .id(questionId)
                            .content((String) row.get("content"))
                            .imageUrl((String) row.get("image_url"))
                            .difficulty(Difficulty.valueOf((String) row.get("difficulty")))
                            .explanation((String) row.get("explanation"))
                            .options(new ArrayList<>())
                            .build()
            );
            OptionResponse optionResponse = OptionResponse.builder()
                    .id(((Number) row.get("option_id")).intValue())
                    .content((String) row.get("option_content"))
                    .correct((boolean) row.get("is_correct"))
                    .build();

            question.getOptions().add(optionResponse);
        }

        List<QuestionResponse> list = new ArrayList<>(questionResponseMap.values());
        return list;
    }
}
