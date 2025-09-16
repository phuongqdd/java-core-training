package com.example.SpringJDBCTemplate.service;

import com.example.SpringJDBCTemplate.model.Student;
import com.example.SpringJDBCTemplate.model.StudentWithCourses;
import com.example.SpringJDBCTemplate.repo.StudentRepo;
import com.example.SpringJDBCTemplate.repo.StudentRepository;
import com.example.SpringJDBCTemplate.repo.StudentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentRepo  studentRepo;
    public List<Student> search(String name, Integer age, String email) {
        Specification<Student> spec = (root, query, cb) -> cb.conjunction();

        if (name != null) {
            spec = spec.and(StudentSpecification.hasName(name));
        }
        if (age != null) {
            spec = spec.and(StudentSpecification.hasAgeGreaterThan(age));
        }
        if (email != null) {
            spec = spec.and(StudentSpecification.hasEmailLike(email));
        }

        return studentRepository.findAll(spec);
    }

    public StudentWithCourses getStudentWithCourses(Long id) {
        return studentRepo.getStudentWithCourses(id);
    }
}
