package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.entity.Course;
import com.dophuong.lms.learning_management_system.entity.Role;
import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.entity.UserCourse;
import com.dophuong.lms.learning_management_system.enums.ErrorCode;
import com.dophuong.lms.learning_management_system.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class CourseJdbcRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoleRepository roleRepository;

    public int addUserToCourse(Long courseId, Long userId, String role){
        String finalRole = role != null ? role : "STUDENT";
        boolean isOwner = false;

        String checkSql = "SELECT COUNT(*) FROM user_course WHERE user_id = ? AND course_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, new Object[]{userId, courseId}, Integer.class);

        if(count != null && count > 0){
            throw new AppException(ErrorCode.USER_ALREADY_ENROLLED);
        }

        Role role1 = roleRepository.findByName(finalRole)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        String insertSql = """
                INSERT INTO user_course (user_id, course_id, role_id, is_owner, enrolled_at)
                VALUES (?, ?, ?, ?, ?)
                """;
        return jdbcTemplate.update(insertSql, userId, courseId, role1.getId(), isOwner, LocalDateTime.now());
    }

    public UserCourse getUserCourse(Long courseId, Long userId){
        String sql = """
            SELECT uc.course_id, c.name, uc.user_id AS u_id, u.username,
                   uc.id, uc.role_id, uc.is_owner, uc.enrolled_at
            FROM user_course uc
            JOIN user u 
                ON uc.user_id = u.user_id
            JOIN course c
                ON c.course_id = uc.course_id
            WHERE uc.course_id = ? AND uc.user_id = ?    
        """;

        return jdbcTemplate.queryForObject(sql, new Object[]{courseId, userId},
                (rs, rowNum) -> {
                    Role role = roleRepository.findById(rs.getInt("role_id"))
                            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

                    User user = new User();
                    user.setId(rs.getLong("u_id"));
                    user.setUsername(rs.getString("username"));

                    Course course = new Course();
                    course.setId(rs.getLong("course_id"));
                    course.setName(rs.getString("name"));

                    UserCourse uc = new UserCourse();
                    uc.setId(rs.getLong("id"));
                    uc.setUser(user);
                    uc.setCourse(course);
                    uc.setRole(role);
                    uc.setIsOwner(rs.getBoolean("is_owner"));
                    uc.setEnrolledAt(rs.getTimestamp("enrolled_at").toLocalDateTime());

                    return uc;
                });
    }

    public boolean checkRoleInCourse(Long courseId, String name) {
        String sql = """
            SELECT r.name
            FROM user_course uc
            JOIN role r ON uc.role_id = r.role_id
            JOIN user u ON uc.user_id = u.user_id
            WHERE uc.course_id = ? AND u.username = ?
            """;

        String role = jdbcTemplate.queryForObject(sql, new Object[]{courseId, name}, String.class);
        return "INSTRUCTOR".equals(role);
    }

}
