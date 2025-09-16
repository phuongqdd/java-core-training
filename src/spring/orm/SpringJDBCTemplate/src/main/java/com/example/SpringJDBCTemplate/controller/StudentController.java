package com.example.SpringJDBCTemplate.controller;

import com.example.SpringJDBCTemplate.model.Student;
import com.example.SpringJDBCTemplate.model.StudentWithCourses;
import com.example.SpringJDBCTemplate.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/search")
    public List<Student> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String email
    ){
        return studentService.search(name, age, email);
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<StudentWithCourses> getStudentWithCourses(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentWithCourses(id));
    }
}
