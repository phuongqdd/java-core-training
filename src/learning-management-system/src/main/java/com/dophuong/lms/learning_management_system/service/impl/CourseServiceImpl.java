package com.dophuong.lms.learning_management_system.service.impl;

import com.dophuong.lms.learning_management_system.dto.request.AddUserToCourseRequest;
import com.dophuong.lms.learning_management_system.dto.request.CourseCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.AddUserToCourseResponse;
import com.dophuong.lms.learning_management_system.dto.response.CourseCreateResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserCourseResponse;
import com.dophuong.lms.learning_management_system.dto.response.UserInCourseResponse;
import com.dophuong.lms.learning_management_system.entity.Course;
import com.dophuong.lms.learning_management_system.entity.Role;
import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.entity.UserCourse;
import com.dophuong.lms.learning_management_system.enums.ErrorCode;
import com.dophuong.lms.learning_management_system.exception.AppException;
import com.dophuong.lms.learning_management_system.mapper.CourseMapper;
import com.dophuong.lms.learning_management_system.mapper.UserCourseMapper;
import com.dophuong.lms.learning_management_system.repository.*;
import com.dophuong.lms.learning_management_system.service.CourseService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserCourseRepository userCourseRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserCourseMapper userCourseMapper;

    @Override
    public Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
    }

    @Override
    public boolean existsById(Long id) {
        return courseRepository.existsById(id);
    }

    @Override
    @Transactional
    public CourseCreateResponse createCourse(CourseCreateRequest request, String name) {
        User owner = userRepository.findByUsername(name)
                .orElseThrow(() -> new RuntimeException("Không tồn tại người dùng"));
//
//        if (owner.getUserRoles() != Role.INSTRUCTOR) {
//            throw new RuntimeException("Người tạo khóa học phải là INSTRUCTOR");
//        }

        Course course = courseMapper.toEntity(request);
        course.setCreatedAt(LocalDateTime.now());

        course = courseRepository.save(course);

        Role role = roleRepository.findByName("INSTRUCTOR")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        UserCourse userCourse = UserCourse.builder()
                .course(course)
                .user(owner)
                .role(role)
                .isOwner(true)
                .enrolledAt(LocalDateTime.now())
                .build();

        userCourseRepository.save(userCourse);
        if (course.getUserCourses() == null) {
            course.setUserCourses(new ArrayList<>());
        }
        course.getUserCourses().add(userCourse);


        return courseMapper.toResponse(course);
    }

    @Override
    public List<UserCourseResponse> getAllCourseById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_EXISTED));
        return userCourseRepository.findAllByCourseForUser(userId);
    }

    @Override
    public List<UserInCourseResponse> getAllUsersInCourse(Long courseId) {
        if(!courseRepository.existsById(courseId))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        return userCourseRepository.findByCourseId(courseId)
                .stream()
                .map(userCourseMapper::toResponse1)
                .collect(Collectors.toList());
    }

    @Override
    public void updateUserRole(Long courseId, Long userId, com.dophuong.lms.learning_management_system.enums.Role role) {

    }

    @Override
    public AddUserToCourseResponse addUserToCourse(Long courseId, AddUserToCourseRequest request) {
        courseRepository.addUserToCourse(courseId, request.getUserId(), request.getRole());
        UserCourse userCourse = courseRepository.getUserCourse(courseId, request.getUserId());
        return userCourseMapper.toResponse(userCourse);
    }

//    @Override
//    public void updateUserRole(Long courseId, Long userId, Role role) {
//        UserCourse userCourse = userCourseRepository.findByCourseIdAndUserId(courseId, userId)
//                .orElseThrow(() -> new RuntimeException("User không tồn tại trong khóa học này"));
//
////        userCourse.setRole(role);
//        userCourseRepository.save(userCourse);
//    }
}
