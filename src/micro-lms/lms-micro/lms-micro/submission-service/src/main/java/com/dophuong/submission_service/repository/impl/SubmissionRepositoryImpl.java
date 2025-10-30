package com.dophuong.submission_service.repository.impl;

import com.dophuong.submission_service.dto.response.*;
import com.dophuong.submission_service.entity.Submission;
import com.dophuong.submission_service.repository.SubmissionRepository;
import com.dophuong.submission_service.repository.feign.QuizClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Repository
public class SubmissionRepositoryImpl implements SubmissionRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private QuizClient quizClient;

    @Override
    public boolean exist(Long id) {
        String sql = """
            SELECT COUNT(*)
            FROM submission
            WHERE submission_id = :id
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Map.of("id", id), Integer.class);
        return count != null && count > 0;
    }

    @Override
    public SubmissionResponse createSubmission(Long courseId, Long quizId, Long userId) {
        int attemptNo = getMaxAttemptNo(quizId, userId) + 1;
        Long submissionId = insertSubmission(quizId, userId, attemptNo);

        List<QuestionResponse> questionDetails = quizClient.getAllQuestionDetailByQuizId(courseId, quizId).getBody();

        saveSubmissionQuestionsAndOptions(questionDetails, submissionId, quizId);

        return traKetQua(courseId, quizId, submissionId);
    }

    @Override
    public SubmissionResponse traKetQua(Long courseId, Long quizId, Long submissionId){
        SubmissionResponse submissionResponse = getSubmissionInfo(submissionId);
        QuizResponse quizResponse = quizClient.getQuiz(courseId, quizId).getBody();
        submissionResponse.setQuizId(quizResponse.getId());
        submissionResponse.setQuizTitle(quizResponse.getTitle());
        submissionResponse.setTimeLimit(quizResponse.getTimeLimit());
        List<SubmissionQuestionResponse> questions = getSubmissionQuestions(submissionId);

        submissionResponse.setQuestions(questions);
        return submissionResponse;
    }

    // -----------------------------
    // Lấy attempt_no lớn nhất
    // -----------------------------
    private int getMaxAttemptNo(Long quizId, Long userId) {
        String sql = """
            SELECT COALESCE(MAX(attempt_no), 0)
            FROM submission
            WHERE quiz_id = :quizId AND user_id = :userId
        """;
        return jdbcTemplate.queryForObject(sql, Map.of("quizId", quizId, "userId", userId), Integer.class);
    }

    // -----------------------------
    // Tạo submission mới
    // -----------------------------
    private Long insertSubmission(Long quizId, Long userId, int attemptNo) {
        String sql = """
            INSERT INTO submission (quiz_id, user_id, attempt_no, status, started_at, duration)
            VALUES (:quizId, :userId, :attemptNo, 'IN_PROGRESS', NOW(), 0)
        """;
        Map<String, Object> params = Map.of("quizId", quizId, "userId", userId, "attemptNo", attemptNo);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder, new String[]{"submission_id"});
        return keyHolder.getKey().longValue();
    }

    // -----------------------------
    // Sinh và lưu câu hỏi + đáp án
    // -----------------------------
    private void saveSubmissionQuestionsAndOptions(List<QuestionResponse> questionDetails, Long submissionId, Long quizId) {
        // Random câu hỏi trước khi lưu
        Collections.shuffle(questionDetails);
        int order = 1;
        for (QuestionResponse questionResponse : questionDetails) {
            Long submissionQuestionId = insertSubmissionQuestion(submissionId, questionResponse, order++);
            saveSubmissionOptions(submissionQuestionId, questionResponse.getOptions());
        }
    }

    // -----------------------------
    // Lưu từng câu hỏi
    // -----------------------------
    private Long insertSubmissionQuestion(Long submissionId, QuestionResponse questionResponse, int order) {
        String sql = """
            INSERT INTO submission_question (submission_id, question_id, question_text, question_order, is_correct, score)
            VALUES (:submissionId, :questionId, :questionText, :questionOrder, 0, 0)
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("submissionId", submissionId);
        params.put("questionId", questionResponse.getId());
        params.put("questionText", questionResponse.getContent());
        params.put("questionOrder", order);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder, new String[]{"submission_question_id"});
        return keyHolder.getKey().longValue();
    }

    // -----------------------------
    // Lưu các lựa chọn (đáp án)
    // -----------------------------
    private void saveSubmissionOptions(Long submissionQuestionId, List<OptionResponse> options) {
        // Random đáp án trước khi lưu
        Collections.shuffle(options);
        String insertSql = """
            INSERT INTO submission_question_option
            (submission_question_id, option_id, option_text, option_order, is_correct, is_chosen)
            VALUES (:submissionQuestionId, :optionId, :optionText, :optionOrder, :isCorrect, false)
        """;

        int order = 1;
        for (OptionResponse optionResponse : options) {
            Map<String, Object> params = new HashMap<>();
            params.put("submissionQuestionId", submissionQuestionId);
            params.put("optionId", optionResponse.getId());
            params.put("optionText", optionResponse.getContent());
            params.put("optionOrder", order++);
            params.put("isCorrect", optionResponse.isCorrect());
            jdbcTemplate.update(insertSql, params);
        }
    }

    // -----------------------------
    // Lấy thông tin submission
    // -----------------------------
    private SubmissionResponse getSubmissionInfo(Long submissionId) {
        String sql = """
            SELECT s.submission_id, s.attempt_no, s.status
            FROM submission s
            WHERE s.submission_id = :submissionId
        """;
        return jdbcTemplate.queryForObject(sql, Map.of("submissionId", submissionId),
                new BeanPropertyRowMapper<>(SubmissionResponse.class));
    }

    // -----------------------------
    // Lấy câu hỏi + đáp án trả về cho client
    // -----------------------------
    private List<SubmissionQuestionResponse> getSubmissionQuestions(Long submissionId) {
        String sql = """
            SELECT sq.submission_question_id, sq.question_text, sq.question_order,
                   sqo.submission_option_id, sqo.option_text, sqo.option_order, sqo.is_chosen AS chosen
            FROM submission_question sq
            JOIN submission_question_option sqo ON sq.submission_question_id = sqo.submission_question_id
            WHERE sq.submission_id = :submissionId
            ORDER BY sq.question_order, sqo.option_order
        """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, Map.of("submissionId", submissionId));

        Map<Long, SubmissionQuestionResponse> questionMap = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            Long sqId = ((Number) row.get("submission_question_id")).longValue();

            SubmissionQuestionResponse question = questionMap.computeIfAbsent(sqId, id ->
                    SubmissionQuestionResponse.builder()
                            .submissionQuestionId(sqId)
                            .questionText((String) row.get("question_text"))
                            .questionOrder(((Number) row.get("question_order")).intValue())
                            .options(new ArrayList<>())
                            .build()
            );

            question.getOptions().add(
                    SubmissionQuestionOptionResponse.builder()
                            .submissionOptionId(((Number) row.get("submission_option_id")).longValue())
                            .optionText((String) row.get("option_text"))
                            .optionOrder(((Number) row.get("option_order")).intValue())
                            .chosen((Boolean) row.get("chosen"))
                            .build()
            );
        }

        return new ArrayList<>(questionMap.values());
    }

    @Override
    public int countQuizAttempts(Long userId, Long quizId) {
        String sql = """
            SELECT COUNT(*)
            FROM submission
            WHERE user_id = :userId AND quiz_id = :quizId
            """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("quizId", quizId);

        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return (count != null) ? count : 0;
    }

    @Override
    public Submission getInProgressSubmission(Long userId, Long quizId) {
        log.warn("User: {} - quizId: {}", userId, quizId);
        String sql = """
        SELECT * FROM submission
        WHERE quiz_id = :quizId
          AND user_id = :userId
          AND status = 'IN_PROGRESS'
    """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    Map.of("quizId", quizId, "userId", userId),
                    new BeanPropertyRowMapper<>(Submission.class)
            );
        } catch (EmptyResultDataAccessException e) {
            return null; // không có submission đang làm
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void finishSubmission(Long courseId, Long quizId, Long submissionId) {
        // Lấy thông tin quiz (để biết giới hạn thời gian)
        QuizResponse quiz = quizClient.getQuiz(courseId, quizId).getBody();

        // Lấy thông tin submission hiện tại
        String sqlSelect = "SELECT * FROM submission WHERE submission_id = :id";
        Submission submission = jdbcTemplate.queryForObject(
                sqlSelect,
                Map.of("id", submissionId),
                new BeanPropertyRowMapper<>(Submission.class)
        );

        // Tính thời gian kết thúc hợp lệ
        LocalDateTime expectedFinish = submission.getStartedAt().plusMinutes(quiz.getTimeLimit());
        LocalDateTime actualFinish = LocalDateTime.now();

        // Nếu người làm bài nộp trễ → vẫn giới hạn trong expectedFinish
        LocalDateTime finalFinish = actualFinish.isAfter(expectedFinish)
                ? expectedFinish
                : actualFinish;

        // Tính duration (tính bằng phút)
        long duration = Duration.between(submission.getStartedAt(), finalFinish).toMinutes();

        // Cập nhật trạng thái và thời gian kết thúc
        String sqlUpdate = """
            UPDATE submission
            SET `status` = 'SUBMITTED',
                ended_at = :endedAt,
                duration = :duration
            WHERE submission_id = :submissionId
              AND `status` = 'IN_PROGRESS'
            """;
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("submissionId", submissionId)
                .addValue("endedAt", Timestamp.valueOf(finalFinish))
                .addValue("duration", duration);

        Map<String, Object> params = Map.of(
                "submissionId", submissionId,
                "endedAt", Timestamp.valueOf(finalFinish),
                "duration", duration
        );

        int rows = jdbcTemplate.update(sqlUpdate, source);
        log.warn("rows: {}", rows);
        log.warn("Transaction status: {}", TransactionSynchronizationManager.isActualTransactionActive());

    }

    @Override
    public SubmissionResponse daoDe(Long quizId, Long userId, int atpList, Long courseId, List<QuestionResponse> questionResponseList) {
        Long submissionId = insertSubmission(quizId, userId, atpList);
        saveSubmissionQuestionsAndOptions(questionResponseList, submissionId, quizId);
        return traKetQua(courseId, quizId, submissionId);
    }

    @Override
    public List<SubmissionResponse> getAllSubmission(List<QuizDetailResponse> quizDetailResponseList) {
        // Nếu không có quiz nào đang mở thì trả về rỗng
        if (quizDetailResponseList == null || quizDetailResponseList.isEmpty()) {
            return List.of();
        }

        // Lấy danh sách quizId
        List<Long> quizIds = quizDetailResponseList.stream()
                .map(QuizDetailResponse::getId)
                .toList();

        String sql = """
            SELECT *
            FROM submission
            WHERE status = 'IN_PROGRESS'
              AND quiz_id IN (:quizIds)
            """;

        List<SubmissionResponse> submissions = jdbcTemplate.query(
                sql,
                Map.of("quizIds", quizIds),
                new BeanPropertyRowMapper<>(SubmissionResponse.class)
        );

        // Lọc lại theo thời gian làm bài của từng quiz
        return submissions.stream()
                // Bước 1: Gán timeLimit cho từng submission dựa vào quiz tương ứng
                .map(submission -> {
                    // Tìm quiz tương ứng với submission hiện tại trong danh sách quiz đang mở
                    QuizDetailResponse quiz = quizDetailResponseList.stream()
                            .filter(q -> q.getId().equals(submission.getQuizId()))
                            .findFirst()
                            .orElse(null);

                    // Nếu tìm thấy quiz → gán thời gian làm bài (timeLimit) cho submission
                    if (quiz != null) {
                        submission.setTimeLimit(quiz.getTimeLimit());
                    }
                    return submission; // trả lại submission (có thể đã gán timeLimit)
                })

                // Bước 2: Lọc những submission hợp lệ (đang trong thời gian làm bài)
                .filter(submission -> {
                    // Nếu không có quiz tương ứng hoặc timeLimit <= 0 → loại bỏ
                    if (submission.getTimeLimit() <= 0) return false;

                    // Lấy thời gian bắt đầu làm bài
                    LocalDateTime startedAt = submission.getStartedAt();

                    // Tính thời gian hết hạn = thời gian bắt đầu + số phút làm bài
                    LocalDateTime expiredAt = startedAt.plusMinutes(submission.getTimeLimit());

                    // Chỉ giữ lại nếu thời gian hiện tại vẫn trước thời điểm hết hạn
                    return LocalDateTime.now().isBefore(expiredAt);
                })

                // Bước 3: Thu kết quả về danh sách
                .toList();
    }

    @Override
    public int getAttemptNo(Long submissionId) {
        String sql = "SELECT attempt_no FROM submission WHERE submission_id = :id";
        try {
            Integer attemptNo = jdbcTemplate.queryForObject(sql, Map.of("id", submissionId), Integer.class);
            return (attemptNo != null) ? attemptNo : 0;
        } catch (EmptyResultDataAccessException e) {
            // Không tìm thấy submission -> trả về 0
            return 0;
        }
    }

    @Override
    public void updateGrade(Long submissionId, double score) {
        String updateSql = """
        UPDATE submission
        SET score = :score,
            ended_at = :endedAt,
            duration = TIMESTAMPDIFF(MINUTE, started_at, :endedAt),
            status = 'SUBMITTED'
        WHERE submission_id = :id
        """;

        jdbcTemplate.update(updateSql, Map.of(
                "score", score,
                "endedAt", LocalDateTime.now(),
                "id", submissionId
        ));
    }

    @Override
    public Submission findById(Long submissionId) {
        String sql = """
                SELECT * FROM submission WHERE submission_id = :id
                """;
        return jdbcTemplate.queryForObject(sql, Map.of("id", submissionId), new BeanPropertyRowMapper<>(Submission.class));
    }


}
