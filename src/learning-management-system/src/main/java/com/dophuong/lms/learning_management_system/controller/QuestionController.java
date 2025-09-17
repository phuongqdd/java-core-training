package com.dophuong.lms.learning_management_system.controller;

import com.dophuong.lms.learning_management_system.entity.Question;
import com.dophuong.lms.learning_management_system.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PreAuthorize("@courseSecurity.hasInstructorOrAdmin(#courseId)")
    @PostMapping("/course/{courseId}")
    public Question addQuestionToCourse(@PathVariable Long courseId, Question question){

    }

}
