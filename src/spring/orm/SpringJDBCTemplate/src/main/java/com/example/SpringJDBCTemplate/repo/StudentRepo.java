package com.example.SpringJDBCTemplate.repo;

import com.example.SpringJDBCTemplate.model.CourseEnrollment;
import com.example.SpringJDBCTemplate.model.Student;
import com.example.SpringJDBCTemplate.model.StudentWithCourses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class StudentRepo {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentRepo.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcTemplate template;

    public StudentRepo(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertStudent(Student student){
        String sql = "INSERT INTO STUDENT (id, name, department) VALUES (:id, :name, :dept)";

        MapSqlParameterSource parmas = new MapSqlParameterSource()
                .addValue("id", student.getId())
                .addValue("name", student.getName());
//                .addValue("dept", student.getDepartment());

        try {
            int rows = jdbcTemplate.update(sql, parmas);
            LOGGER.info("Inserted {} student record(s)", rows);
        }catch (DataAccessException e){
            LOGGER.error("Failed to insert student ", e);
            throw e;
        }
    }

    public Optional<Student> findById(int id){
        String sql = "SELECT id, name, department FROM STUDENT WHERE id =:id";

        try{
//            Student student = jdbcTemplate.queryForObject(
//                    sql,
//                    Collections.singletonMap("id", id),
//                    (rs, rowNum) -> new Student(
//                            rs.getInt("id"),
//                            rs.getString("name"),
//                            rs.getString("department")
//                    )
//            );
            Student student = null;
            return Optional.ofNullable(student);
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public StudentWithCourses getStudentWithCourses(Long id){
        String sql = "CALL GetStudentWithCourses(?)";

        List<StudentWithCourses> result = template.query(
                sql,
                new Object[]{id},
                (rs, rowNum) -> {
                    CourseEnrollment course = new CourseEnrollment();
                    course.setCourseId(rs.getLong("course_id"));
                    course.setCourseName(rs.getString("course_name"));
                    course.setCredit(rs.getInt("credit"));
                    course.setGrade(rs.getString("grade"));

                    // mapping student
                    StudentWithCourses student = new StudentWithCourses();
                    student.setStudentId(rs.getLong("student_id"));
                    student.setStudentName(rs.getString("student_name"));
                    student.setAge(rs.getInt("age"));
                    student.setEmail(rs.getString("email"));
                    student.setCourses(new ArrayList<>(List.of(course)));

                    return student;
                }

        );

        // gộp courses vào 1 student duy nhất
        if (result.isEmpty()) {
            return null;
        }

        StudentWithCourses student = result.get(0);
        List<CourseEnrollment> courses = new ArrayList<>();
        for (StudentWithCourses row : result) {
            courses.addAll(row.getCourses());
        }
        student.setCourses(courses);

        return student;
    }

}
