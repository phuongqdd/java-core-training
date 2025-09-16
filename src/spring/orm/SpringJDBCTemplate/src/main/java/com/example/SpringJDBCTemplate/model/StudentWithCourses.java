package com.example.SpringJDBCTemplate.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StudentWithCourses {
    private Long studentId;
    private String studentName;
    private Integer age;
    private String email;
    private List<CourseEnrollment> courses;
}
