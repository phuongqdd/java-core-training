package com.dophuong.course_service.repository.impl;

import com.dophuong.course_service.dto.response.RoleResponse;
import com.dophuong.course_service.entity.Course;
import com.dophuong.course_service.entity.UserCourse;
import com.dophuong.course_service.enums.ErrorCode;
import com.dophuong.course_service.enums.Role;
import com.dophuong.course_service.exception.AppException;
import com.dophuong.course_service.repository.CourseRepository;
import com.dophuong.course_service.repository.feign.RoleClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class CourseRepositoryImpl implements CourseRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoleClient roleClient;

    @Override
    public Course save(Course course) {
        if(course.getCreatedAt() == null){
            course.setCreatedAt(LocalDateTime.now());
        }

        course.setUpdatedAt(LocalDateTime.now());

        if(course.getId() == null){
            String sql = """
                    INSERT INTO course(capacity, created_at, description, is_published,
                                        name, thumbnail_url, updated_at)
                    VALUES(?, ?, ?, ?, ?, ?, ?)
                    """;

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, course.getCapacity());
                statement.setTimestamp(2, Timestamp.valueOf(course.getCreatedAt()));
                statement.setString(3, course.getDescription());
                statement.setBoolean(4, course.getPublished());
                statement.setString(5, course.getName());
                statement.setString(6, course.getThumbnailUrl());
                statement.setTimestamp(7, Timestamp.valueOf(course.getUpdatedAt()));
                return statement;
            }, keyHolder);
            course.setId(keyHolder.getKey().longValue());
        }else{
            String sql = """
                    UPDATE course
                    SET capacity = ?, created_at = ?, description = ?, is_published = ?,
                        name = ?, thumbnail_url = ?, updated_at = ?
                    WHERE course_id = ?
                    """;
            jdbcTemplate.update(sql,
                    course.getCapacity(),
                    Timestamp.valueOf(course.getCreatedAt()),
                    course.getDescription(),
                    course.getPublished(),
                    course.getName(),
                    course.getThumbnailUrl(),
                    Timestamp.valueOf(course.getUpdatedAt()),
                    course.getId()
            );
        }
        return course;
    }

    @Override
    public Optional<Course> findById(Long id) {
        String sql = "SELECT * FROM course WHERE course_id = ?";
        try {
            Course course = jdbcTemplate.queryForObject(sql,
                    new BeanPropertyRowMapper<>(Course.class), id);
            return Optional.ofNullable(course);
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Course> findAll() {
        String sql = "SELECT * FROM course";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Course.class));
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM course WHERE id = ?", id);
    }

    @Override
    public void addUserToCourse(Long courseId, Long userId, String role){
        String finalRole = (role != null && !role.trim().isEmpty()) ? role.trim() : "STUDENT";
        boolean isOwner = false;

        String checkSql = "SELECT COUNT(*) FROM user_course WHERE user_id = ? AND course_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, new Object[]{userId, courseId}, Integer.class);

        if(count != null && count > 0){
            throw new AppException(ErrorCode.USER_ALREADY_ENROLLED);
        }

        RoleResponse role1;
        try {
            ResponseEntity<RoleResponse> response = roleClient.getByRoleName(finalRole);
            role1 = response.getBody();
            String insertSql = """
                INSERT INTO user_course (user_id, course_id, role_id, is_owner, enrolled_at)
                VALUES (?, ?, ?, ?, ?)
                """;
            jdbcTemplate.update(insertSql, userId, courseId, role1.getId(), isOwner, LocalDateTime.now());
        } catch (FeignException.BadRequest e) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
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
//                    Role role = roleRepository.findById(rs.getInt("role_id"))
//                            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

//                    User user = new User();
//                    user.setId(rs.getLong("u_id"));
//                    user.setUsername(rs.getString("username"));
//
//                    Course course = new Course();
//                    course.setId(rs.getLong("course_id"));
//                    course.setName(rs.getString("name"));

                    UserCourse uc = new UserCourse();
                    uc.setId(rs.getLong("id"));
//                    uc.setUserId(userd);
//                    uc.setCourseId(courseId);
//                    uc.setRole(role);
                    uc.setIsOwner(rs.getBoolean("is_owner"));
                    uc.setEnrolledAt(rs.getTimestamp("enrolled_at").toLocalDateTime());

                    return uc;
                });
    }

    public boolean checkRoleInCourse(Long courseId, Long userId) {
        String sql = """
            SELECT uc.role_id
            FROM user_course uc
            WHERE uc.course_id = ? AND uc.user_id = ?
            """;
        int roleId = jdbcTemplate.queryForObject(sql, new Object[]{courseId, userId}, Integer.class);
        String roleName = roleClient.getRoleById(roleId).getBody().getName();
        return "INSTRUCTOR".equals(roleName);
    }

    @Override
    public boolean checkUserInCourse(Long courseId, Long userId) {
        String sql = """
                SELECT COUNT(*)
                FROM user_course uc
                WHERE uc.course_id = ? AND uc.user_id = ?
                """;

        Integer cnt = jdbcTemplate.queryForObject(sql, Integer.class, courseId, userId);
        return cnt != null && cnt > 0;
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM course WHERE course_id = ?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void updateUserRole(Long courseId, Long userId, String role) {
        RoleResponse role1;
        try {
            ResponseEntity<RoleResponse> response = roleClient.getByRoleName(role);
            role1 = response.getBody();
            String insertSql = """
                UPDATE user_course
                SET role_id = ?
                WHERE user_id = ? AND course_id = ?
                """;
            jdbcTemplate.update(insertSql, role1.getId(), userId, courseId);
        } catch (FeignException.BadRequest e) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
    }

    @Override
    public List<Long> getAllCourseIds() {
        String sql = "SELECT course_id FROM course";
        return jdbcTemplate.queryForList(sql, Long.class);
    }
}
