package com.example.SpringJDBCTemplate;

import com.example.SpringJDBCTemplate.config.AppConfig;
import com.example.SpringJDBCTemplate.model.Alien;
import com.example.SpringJDBCTemplate.model.Student;
import com.example.SpringJDBCTemplate.repo.ALienRepo;
import com.example.SpringJDBCTemplate.repo.StudentRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

@SpringBootApplication
@Slf4j
public class SpringJdbcTemplateApplication {

	public static void main(String[] args) {
		ApplicationContext context =  SpringApplication.run(SpringJdbcTemplateApplication.class, args);

//		Alien alien = context.getBean(Alien.class);
//		alien.setId(100);
//		alien.setName("Phuc");
//		alien.setTech("Java");
//
//		ALienRepo repo = context.getBean(ALienRepo.class);
//		repo.save(alien);
//
//		System.out.println(repo.findAll());
//		AnnotationConfigApplicationContext context1 = null;
//
//		try {
//			context1 = new AnnotationConfigApplicationContext(AppConfig.class);
//			StudentRepo studentRepo = context1.getBean(StudentRepo.class);
//
//			// Insert a new student
//			Student newStudent = new Student(1, "Alice", "Computer Science");
//			studentRepo.insertStudent(newStudent);
//
//			// Retrieve the student
//			Optional<Student> retrieved = studentRepo.findById(1);
//			retrieved.ifPresentOrElse(
//					student -> log.info("Found student: {}", student),
//					() -> log.warn("Student not found")
//			);
//
//		} catch (Exception e) {
//			log.error("Application error", e);
//		} finally {
//			if (context1 != null) {
//				context1.close();
//			}
//		}
	}

}
