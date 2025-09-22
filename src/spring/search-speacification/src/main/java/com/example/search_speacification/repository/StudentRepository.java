package com.example.search_speacification.repository;

import com.example.search_speacification.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    List<Student> findByName(String name);
    List<Student> findByAddressCity(String city);
    List<Student> findBySubjectsName(String subName);
}
