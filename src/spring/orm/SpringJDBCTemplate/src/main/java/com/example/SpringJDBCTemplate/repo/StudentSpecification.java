package com.example.SpringJDBCTemplate.repo;

import com.example.SpringJDBCTemplate.model.Student;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {
    public static Specification<Student> hasName(String name){
        return (root, query, cb) ->
                name == null ? null : cb.equal(root.get("name"), name);
    }

    public static Specification<Student> hasAgeGreaterThan(Integer age){
        return (root, query, cb) ->
                age == null ? null : cb.greaterThan(root.get("age"), age);
    }

    public static Specification<Student> hasEmailLike(String email){
        return (root, query, cb) ->
                email == null ? null : cb.like(root.get("email"), "%" + email + "%");
    }
}
