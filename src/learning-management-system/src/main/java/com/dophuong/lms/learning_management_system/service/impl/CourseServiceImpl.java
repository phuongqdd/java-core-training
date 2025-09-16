package com.dophuong.lms.learning_management_system.service.impl;

import com.dophuong.lms.learning_management_system.dto.request.CourseCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.CourseCreateResponse;
import com.dophuong.lms.learning_management_system.dto.response.CourseResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserCourseResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserInCourseResponse;
import com.dophuong.lms.learning_management_system.entity.Course;
import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.entity.UserCourse;
import com.dophuong.lms.learning_management_system.enums.Role;
import com.dophuong.lms.learning_management_system.mapper.CourseMapper;
import com.dophuong.lms.learning_management_system.mapper.UserCourseMapper;
import com.dophuong.lms.learning_management_system.mapper.UserInCourseMapper;
import com.dophuong.lms.learning_management_system.repository.CourseRepository;
import com.dophuong.lms.learning_management_system.repository.UserCourseRepository;
import com.dophuong.lms.learning_management_system.repository.UserRepository;
import com.dophuong.lms.learning_management_system.service.CourseService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserCourseMapper userCourseMapper;

    @Autowired
    private UserInCourseMapper userInCourseMapper;

    @Override
    @Transactional
    public CourseCreateResponse createCourse(CourseCreateRequest request) {
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Instructor không tồn tại"));
//
//        if (owner.getUserRoles() != Role.INSTRUCTOR) {
//            throw new RuntimeException("Người tạo khóa học phải là INSTRUCTOR");
//        }

        log.info("trngj thai: " + request.getIsPublished());

        Course course = courseMapper.toEntity(request);
        course.setCreatedAt(LocalDateTime.now());
        log.info("trngj thai 1: " + course.getIsPublished());



        course = courseRepository.save(course);

        UserCourse userCourse = UserCourse.builder()
                .course(course)
                .user(owner)
                .role(Role.INSTRUCTOR)
                .isOwner(true)
                .enrolledAt(LocalDateTime.now())
                .build();

        userCourseRepository.save(userCourse);
        course.getUserCourses().add(userCourse);

        return courseMapper.toResponse(course);
    }

    @Override
    public List<UserCourseResponse> getAllCourseById(Long id) {
        return userCourseRepository.findByUserId(id)
                .stream()
                .map(userCourseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserInCourseResponse> getAllUsersInCourse(Long courseId) {
        return userCourseRepository.findByCourseId(courseId)
                .stream()
                .map(userInCourseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addUserToCourse(Long courseId, Long userId, Role role) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với id = "  + courseId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id = " + userId));

        if(userCourseRepository.existsByUserIdAndCourseId(userId, courseId)){
            throw new RuntimeException("Người dùng đã tham gia khóa học na rồi!");
        }

        UserCourse userCourse = UserCourse.builder()
                .user(user)
                .course(course)
                .role(role)
                .enrolledAt(LocalDateTime.now())
                .isOwner(false)
                .build();
        userCourseRepository.save(userCourse);
    }

    @Override
    public void updateUserRole(Long courseId, Long userId, Role role) {
        UserCourse userCourse = userCourseRepository.findByCourseIdAndUserId(courseId, userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại trong khóa học này"));

        userCourse.setRole(role);
        userCourseRepository.save(userCourse);
    }
}
