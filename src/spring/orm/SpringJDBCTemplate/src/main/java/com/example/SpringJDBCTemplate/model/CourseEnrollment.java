package com.example.SpringJDBCTemplate.model;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CourseEnrollment {
    private Long courseId;
    private String courseName;
    private Integer credit;
    private String grade;
}
