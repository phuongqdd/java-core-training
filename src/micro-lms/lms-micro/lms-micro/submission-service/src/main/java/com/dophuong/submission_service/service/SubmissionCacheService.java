package com.dophuong.submission_service.service;

import com.dophuong.submission_service.dto.response.*;
import com.dophuong.submission_service.entity.SubmissionQuestion;
import com.dophuong.submission_service.entity.SubmissionQuestionOption;
import com.dophuong.submission_service.mapper.QuizMapper;
import com.dophuong.submission_service.mapper.SubmissionQuestionMapper;
import com.dophuong.submission_service.mapper.SubmissionQuestionOptionMapper;
import com.dophuong.submission_service.repository.SubmissionQuestionOptionRepository;
import com.dophuong.submission_service.repository.SubmissionQuestionRepository;
import com.dophuong.submission_service.repository.SubmissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class SubmissionCacheService {

    private static final String CACHE_PREFIX_SUBMISSION = "submission:quiz:";
    private static final String CACHE_PREFIX_QUIZ = "quiz:template:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private SubmissionQuestionRepository submissionQuestionRepository;
    @Autowired
    private SubmissionQuestionOptionRepository submissionQuestionOptionRepository;

    @Autowired
    private QuizMapper quizMapper;
    @Autowired
    private SubmissionQuestionMapper submissionQuestionMapper;
    @Autowired
    private SubmissionQuestionOptionMapper submissionQuestionOptionMapper;

    @Scheduled(fixedRate = 300_000)
    public void preloadSubmissions(){
        List<QuizDetailResponse> quizDetailResponseList = getAllCachedQuizzes();
        List<SubmissionResponse> submissionResponses = getAllSubmission(quizDetailResponseList);
        for(SubmissionResponse submissionResponse : submissionResponses){
            List<SubmissionQuestion> questionResponseList = submissionQuestionRepository.findBySubmissionId(submissionResponse.getSubmissionId());
            List<SubmissionQuestionResponse> questionResponses = submissionQuestionMapper.toResponseList(questionResponseList);
            for(SubmissionQuestionResponse questionResponse : questionResponses){
                List<SubmissionQuestionOption> submissionQuestionOptions = submissionQuestionOptionRepository.findBySubQuesId(questionResponse.getSubmissionQuestionId());
                List<SubmissionQuestionOptionResponse> optionResponses = submissionQuestionOptionMapper.toResponseList(submissionQuestionOptions);
                questionResponse.setOptions(optionResponses);
            }
            submissionResponse.setQuestions(questionResponses);
            String key = CACHE_PREFIX_SUBMISSION + submissionResponse.getQuizId() + ":user:" + submissionResponse.getUserId();
            redisTemplate.opsForValue().set(
                    key,
                    submissionResponse,
                    Duration.ofMinutes(submissionResponse.getTimeLimit())
            );
        }

    }

    private List<SubmissionResponse> getAllSubmission(List<QuizDetailResponse> quizDetailResponseList) {
        return submissionRepository.getAllSubmission(quizDetailResponseList);
    }

    public List<QuizDetailResponse> getAllCachedQuizzes() {
        Set<String> keys = redisTemplate.keys(CACHE_PREFIX_QUIZ + "*");

        List<QuizDetailResponse> quizzes = new ArrayList<>();

        if (keys != null) {
            for (String key : keys) {
                com.example.common_service.dto.response.QuizDetailResponse quiz = (com.example.common_service.dto.response.QuizDetailResponse) redisTemplate.opsForValue().get(key);
                if (quiz != null) {
                    QuizDetailResponse response = quizMapper.toResponse(quiz);
                    quizzes.add(response);
                }
            }
        }

        return quizzes;
    }
}
