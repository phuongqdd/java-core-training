package com.example.search_speacification.controller;

import com.example.search_speacification.dto.Request;
import com.example.search_speacification.entity.Student;
import com.example.search_speacification.repository.StudentRepository;
import com.example.search_speacification.service.FiltersSpecification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filter")
public class FilterController {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FiltersSpecification<Student> studentFiltersSpecification;

    @GetMapping("/{name}")
    public List<Student> getStudentByName(@PathVariable(name = "name") String name){
        return studentRepository.findByName(name);
    }

    @GetMapping("/city/{city}")
    public List<Student> getStudentByCity(@PathVariable(name = "city") String city){
        return studentRepository.findByAddressCity(city);
    }

    @GetMapping("/subject/{subject}")
    public List<Student> getStudentBySubject(@PathVariable(name = "subject") String subject){
        return studentRepository.findBySubjectsName(subject);
    }

//    @PostMapping("/specification")
//    public List<Student> getStudents(){
//        Specification<Student> specification = new Specification<>() {
//
//            @Override
//            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                return criteriaBuilder.equal(root.get("name"), "Bùi Quang Long");
//            }
//        };
//
//        List<Student> all = studentRepository.findAll(specification);
//        return all;
//    }

    @PostMapping("/specification")
    public List<Student> getStudents(@RequestBody Request request){
        Specification<Student> specification = studentFiltersSpecification.getSearchSpecification(request.getRequest());

        List<Student> all = studentRepository.findAll(specification);
        return all;
    }

}
