package com.dophuong.lms.learning_management_system.repository.impl;

import com.dophuong.lms.learning_management_system.entity.Role;
import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.entity.UserCourse;
import com.dophuong.lms.learning_management_system.repository.UserCourseJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Service
public class UserCourseRepositoryImpl implements UserCourseJdbcRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public UserCourse save(UserCourse userCourse) {
        if(userCourse.getId() == null){
            String sql = """
                    INSERT INTO user_course(enrolled_at, is_owner, course_id, user_id, role_id)
                    VALUES (?, ?, ?, ?, ?)
                    """;

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setTimestamp(1, Timestamp.valueOf(userCourse.getEnrolledAt()));
                ps.setBoolean(2, userCourse.getIsOwner());
                ps.setLong(3, userCourse.getCourse().getId());
                ps.setLong(4, userCourse.getUser().getId());
                ps.setInt(5, userCourse.getRole().getId());
                return ps;
            }, keyHolder);
            userCourse.setId(keyHolder.getKey().longValue());
        }else {
            String sql = """
                UPDATE user_course
                   SET enrolled_at = ?, is_owner = ?, course_id = ?, user_id = ?, role_id = ?
                 WHERE id = ?
                """;
            jdbcTemplate.update(sql,
                    Timestamp.valueOf(userCourse.getEnrolledAt()),
                    userCourse.getIsOwner(),
                    userCourse.getCourse().getId(),
                    userCourse.getUser().getId(),
                    userCourse.getRole().getId(),
                    userCourse.getId()
            );
        }
        return userCourse;
    }

    @Override
    public UserCourse findById(Long id) {
        String sql = "SELECT * FROM user_course WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(UserCourse.class), id);
    }

    @Override
    public List<UserCourse> findByCourseId(Long courseId) {
        String sql = """
        SELECT uc.id as uc_id, uc.enrolled_at, uc.is_owner,
               u.user_id as u_id, u.username, u.full_name, u.email,
               r.role_id as r_id, r.name as role_name
        FROM user_course uc
        JOIN user u ON uc.user_id = u.user_id
        JOIN role r ON uc.role_id = r.role_id
        WHERE uc.course_id = ?
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserCourse uc = new UserCourse();
            uc.setId(rs.getLong("uc_id"));
            uc.setEnrolledAt(rs.getTimestamp("enrolled_at").toLocalDateTime());
            uc.setIsOwner(rs.getBoolean("is_owner"));

            // map user
            User user = new User();
            user.setId(rs.getLong("u_id"));
            user.setUsername(rs.getString("username"));
            user.setFullName(rs.getString("full_name"));
            user.setEmail(rs.getString("email"));
            uc.setUser(user);

            // map role
            Role role = new Role();
            role.setId(rs.getInt("r_id"));
            role.setName(rs.getString("role_name"));
            uc.setRole(role);

            return uc;
        }, courseId);
    }

    @Override
    public List<UserCourse> findByUserId(Long userId) {
        String sql = "SELECT * FROM user_course WHERE user_id = ?";
        return jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(UserCourse.class),
                userId);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM user_course WHERE id=?";
        jdbcTemplate.update(sql, id);
    }
}
