package com.dophuong.lms.learning_management_system.repository.impl;

import com.dophuong.lms.learning_management_system.dto.response.SubmissionResponse;
import com.dophuong.lms.learning_management_system.repository.SubmissionRepository;
import com.dophuong.lms.learning_management_system.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SubmissionRepositoryImpl implements SubmissionRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public SubmissionResponse createSubmission(Long quizId, Long courseId) {
        String sql = "";
        return SubmissionResponse.builder().build();
    }
}
