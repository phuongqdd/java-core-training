package com.dophuong.lms.learning_management_system.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionNJDPCRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public int addQuestion(){

    }
}
