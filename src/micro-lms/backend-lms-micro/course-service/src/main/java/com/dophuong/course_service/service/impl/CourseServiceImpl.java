package com.dophuong.course_service.service.impl;

import com.dophuong.course_service.dto.request.AddUserToCourseRequest;
import com.dophuong.course_service.dto.request.CourseCreateRequest;
import com.dophuong.course_service.dto.response.*;
import com.dophuong.course_service.entity.Course;
import com.dophuong.course_service.entity.UserCourse;
import com.dophuong.course_service.enums.ErrorCode;
import com.dophuong.course_service.enums.Role;
import com.dophuong.course_service.exception.AppException;
import com.dophuong.course_service.mapper.CourseMapper;
import com.dophuong.course_service.repository.CourseRepository;
import com.dophuong.course_service.repository.UserCourseRepository;
import com.dophuong.course_service.repository.feign.RoleClient;
import com.dophuong.course_service.repository.feign.UserClient;
import com.dophuong.course_service.service.CourseService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserCourseRepository userCourseRepository;
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private RoleClient roleClient;

    private String getUserName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

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
    public CourseResponse createCourse(CourseCreateRequest request, String username) {
        UserResponse owner = userClient.getByUsername(username).getBody();

        Course course = courseMapper.toEntity(request);
        course.setCreatedAt(LocalDateTime.now());

        course = courseRepository.save(course);

        int roleId = roleClient.getByRoleName(username).getBody().getId();

        UserCourse userCourse = UserCourse.builder()
                .courseId(course.getId())
                .userId(owner.getId())
                .roleId(roleId)
                .isOwner(true)
                .enrolledAt(LocalDateTime.now())
                .build();

        userCourseRepository.save(userCourse);

        return courseMapper.toResponse(course);
    }

    @Override
    public List<UserCourseResponse> getAllCourse() {
        Long userId = Objects.requireNonNull(userClient.getByUsername(getUserName()).getBody()).getId();
        return userCourseRepository.findAllByCourseForUser(userId);
    }

    @Override
    public List<UserInCourseResponse> getAllUsersInCourse(Long courseId) {
//        if(!courseRepository.existsById(courseId))
//            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
//
//        return userCourseRepository.findByCourseId(courseId)
//                .stream()
//                .map(userCourseMapper::toResponse1)
//                .collect(Collectors.toList());
        return List.of();
    }

    @Override
    public void updateUserRole(Long courseId, Long userId, String role) {
        existsById(courseId);
        courseRepository.updateUserRole(courseId, userId, role);
    }

    @Override
    public void addUserToCourse(Long courseId, AddUserToCourseRequest request) {
        existsById(courseId);
        courseRepository.addUserToCourse(courseId, request.getUserId(), request.getRole());
    }

    @Override
    public boolean checkRoleInCourse(Long courseId, String username) {
        Long userId = userClient.getByUsername(username).getBody().getId();

        log.warn("Check role: {}", userId);
        return courseRepository.checkRoleInCourse(courseId, userId);
    }

    @Override
    public boolean checkUserInCourse(Long courseId, String username) {
        Long userId = userClient.getByUsername(username).getBody().getId();
        log.warn("Check user: {}", userId);
        return courseRepository.checkUserInCourse(courseId, userId);
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
