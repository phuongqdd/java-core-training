package com.dophuong.lms.learning_management_system.controller;

import com.dophuong.lms.learning_management_system.dto.request.AddQuestionToQuizRequest;
import com.dophuong.lms.learning_management_system.dto.request.QuizCreateRequest;
import com.dophuong.lms.learning_management_system.dto.request.QuizUpdateRequest;
import com.dophuong.lms.learning_management_system.dto.response.*;
import com.dophuong.lms.learning_management_system.service.QuizExportService;
import com.dophuong.lms.learning_management_system.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/quizzes")
public class QuizController {
    @Autowired
    private QuizService quizService;
    @Autowired
    private QuizExportService quizExportService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isUserInCourse(#courseId)")
    public ResponseEntity<ApiResponse<List<QuizSummaryResponse>>> getQuizzes(
            @PathVariable(name = "courseId") Long courseId
    ){
        List<QuizSummaryResponse> quizResponses = quizService.getQuizzes(courseId);
        return ResponseEntity.ok(ApiResponse.<List<QuizSummaryResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách quiz thành công")
                        .data(quizResponses)
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<QuizResponse>> createQuiz(
            @PathVariable(name = "courseId") Long courseId,
            @Valid @RequestBody QuizCreateRequest request
            ){
        QuizResponse quizResponse = quizService.createQuiz(courseId, request);
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(ApiResponse.<QuizResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Tạo mới bài kiểm tra thành công")
                        .data(quizResponse)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/{quizId}/details")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<QuizDetailResponse>> getQuizDetail(
            @PathVariable(name = "courseId") Long courseId,
            @PathVariable(name = "quizId") Long quizId
    ){
        QuizDetailResponse quizDetailResponse = quizService.getQuizDetail(courseId, quizId);
        return ResponseEntity.ok(ApiResponse.<QuizDetailResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy bài kiểm tra thành công")
                        .data(quizDetailResponse)
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{quizId}/questions/{questionId}")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<Void>> deleteQuestionFromQuiz(
            @PathVariable(name = "courseId") Long courseId,
            @PathVariable(name = "quizId") Long quizId,
            @PathVariable(name = "questionId") Long questionId
    ){
        quizService.deleteQuestionFromQuiz(courseId, quizId, questionId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .message("Xóa câu hỏi thành công")
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/{quizId}/questions")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<Void>> addQuestionToQuiz(
            @PathVariable(name = "courseId") Long courseId,
            @PathVariable(name = "quizId") Long quizId,
            @Valid @RequestBody AddQuestionToQuizRequest request){
        quizService.addQuestionToQuiz(courseId, quizId, request);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Thêm câu hỏi vào bài kiểm tra thành công")
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{quizId}")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<Void>> updateQuiz(
            @PathVariable(name = "courseId") Long courseId,
            @PathVariable(name = "quizId") Long quizId,
            @Valid @RequestBody QuizUpdateRequest request) {

        quizService.updateQuiz(courseId, quizId, request);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật quiz thành công")
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{quizId}/export-word")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<QuizExportResponse>> exportQuizToWord(
            @PathVariable(name = "courseId") Long courseId,
            @PathVariable(name = "quizId") Long quizId
    ){
        QuizDetailResponse quizDetail = quizService.getQuizDetail(courseId, quizId);
        QuizExportResponse response = quizExportService.exportQuizToWord(quizDetail);

        return ResponseEntity.ok(ApiResponse.<QuizExportResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Xuất bài kiểm tra thành công")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{quizId}")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.hasInstructorInCourse(#courseId)")
    public ResponseEntity<ApiResponse<Void>> deleteQuiz(
            @PathVariable(name = "courseId") Long courseId,
            @PathVariable(name = "quizId") Long quizId
    ){
        quizService.deleteQuiz(courseId, quizId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Xóa quiz thành công")
                .timestamp(LocalDateTime.now())
                .build());
    }

}
