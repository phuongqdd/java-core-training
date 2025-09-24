-- Tạo random câu hỏi

DELIMITER $$

CREATE PROCEDURE add_questions_for_level(
    IN p_quiz_id BIGINT,
    IN p_level ENUM('EASY','MEDIUM','HARD','VERY_HARD'),
    IN p_total INT
)
BEGIN
    DECLARE v_question_id BIGINT;

    WHILE p_total > 0 DO
        SELECT question_id INTO v_question_id
        FROM question
        WHERE difficulty = p_level
          AND question_id NOT IN (
              SELECT question_id
              FROM quiz_question
              WHERE quiz_id = p_quiz_id
          )
        ORDER BY RAND()
        LIMIT 1;

        INSERT INTO quiz_question(quiz_id, question_id)
        VALUES (p_quiz_id, v_question_id);

        SET p_total = p_total - 1;
    END WHILE;
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE create_quiz_random(
    IN p_quiz_id BIGINT,
    IN p_user_id BIGINT,
    IN p_total_rl INT,
    IN p_total_un INT,
    IN p_total_ap INT,
    IN p_total_an INT
)
BEGIN
    -- Thêm câu hỏi theo từng mức độ
    CALL add_questions_for_level(p_quiz_id, 'EASY', p_total_rl);
    CALL add_questions_for_level(p_quiz_id, 'MEDIUM', p_total_un);
    CALL add_questions_for_level(p_quiz_id, 'HARD', p_total_ap);
    CALL add_questions_for_level(p_quiz_id, 'VERY_HARD', p_total_an);

    -- Ghi lịch sử tạo quiz
    INSERT INTO quiz_history(quiz_id, user_id, action_type, action_time)
    VALUES (p_quiz_id, p_user_id, 'CREATED', NOW(6));
END $$

DELIMITER ;

-- Tạo thêm 1 câu hỏi vào quiz

use lms_db;

DELIMITER $$

CREATE PROCEDURE add_question_to_quiz(
    IN p_quiz_id BIGINT,
    IN p_question_id BIGINT,
    IN p_user_id BIGINT
)
BEGIN
    -- 1. Thêm vào bảng quiz_question
    INSERT IGNORE INTO quiz_question(quiz_id, question_id)
    VALUES (p_quiz_id, p_question_id);
    
    -- 2. Cập nhật lại quiz
    UPDATE quiz q
    JOIN(
        SELECT qq.quiz_id, COUNT(*) AS total,
            SUM(CASE WHEN ques.difficulty = 'EASY' THEN 1 ELSE 0 END) AS rl,
            SUM(CASE WHEN ques.difficulty = 'MEDIUM' THEN 1 ELSE 0 END) AS un,
            SUM(CASE WHEN ques.difficulty = 'HARD' THEN 1 ELSE 0 END) AS ap,
            SUM(CASE WHEN ques.difficulty = 'VERY_HARD' THEN 1 ELSE 0 END) AS an
        FROM quiz_question qq 
        JOIN question ques ON qq.question_id = ques.question_id
        WHERE qq.quiz_id = p_quiz_id
        GROUP BY qq.quiz_id
    ) t ON q.quiz_id = t.quiz_id
    SET q.total = t.total,
        q.pct_rl = CASE WHEN t.total = 0 THEN 0 ELSE ROUND(t.rl * 100.0 / t.total) END,
        q.pct_un = CASE WHEN t.total = 0 THEN 0 ELSE ROUND(t.un * 100.0 / t.total) END,
        q.pct_ap = CASE WHEN t.total = 0 THEN 0 ELSE ROUND(t.ap * 100.0 / t.total) END,
        q.pct_an = CASE WHEN t.total = 0 THEN 0 ELSE ROUND(t.an * 100.0 / t.total) END,
        q.updated_at = NOW();
    
    -- 3. Ghi log
    INSERT INTO quiz_history(action_type, action_time, quiz_id, user_id)
    VALUES('UPDATED', NOW(), p_quiz_id, p_user_id);
END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE delete_quiz(
    IN p_quiz_id BIGINT
)
BEGIN
    DELETE FROM quiz_question
    WHERE quiz_id = p_quiz_id;

    DELETE FROM quiz_history
    WHERE quiz_id = p_quiz_id;

    DELETE FROM quiz
    WHERE id = p_quiz_id;
END $$

DELIMITER ;

-- tạo 1 submission
DELIMITER $$
CREATE PROCEDURE create_submission(
	IN p_quiz_id BIGINT,
    IN p_user_id BIGINT
)
BEGIN
	DECLARE v_submission_id BIGINT;
    DECLARE V_attempt_no INT;
    DECLARE done INT DEFAULT 0;
    DECLARE v_question_id BIGINT;
    DECLARE v_option_id BIGINT;
    
    -- Tính lần làm thứ mấy
    SELECT COALESCE(MAX(attempt_no), 0) + 1
    INTO v_attempt_no
    FROM submission
    WHERE quiz_id = p_quiz_id AND user_id = p_user_id;
    
    -- Tạo submission
    INSERT INTO submission(quiz_id, user_id, attempt_no, status, started_at, duration)
    VALUES(p_quiz_id, p_user_id, v_attempt_no, "IN_PROGRESS", NOW(), 0);
    
    SET v_submission_id = LAST_INSERT_ID();
    
    -- Sinh câu hỏi từ quiz gốc (trộn câu hỏi)
    WITH shuffed_question AS(
		SELECT q.question_id, q.content
        FROM quiz_question qq JOIN question q
			ON qq.quiz_id = p_quiz_id
		ORDER BY RAND()
    )
    INSERT INTO submission_question(submission_id, question_id, question_text, question_order, is_correct, score)
    SELECT v_submission_id, question_id, content, ROW_NUMBER() OVER() AS question_order, 0, 0
    FROM shuffed_question;
    
    
    -- Đảo cả đáp án
    WITH submission_questions AS (
        SELECT submission_question_id, question_id
        FROM submission_question
        WHERE submission_id = v_submission_id
    )
    INSERT INTO submission_question_option(submission_question_id, option_id, option_text, option_order, is_correct, is_chosen)
    SELECT 
        sq.submission_question_id,
        o.id,
        o.content,
        ROW_NUMBER() OVER (PARTITION BY sq.submission_question_id ORDER BY RAND()) AS option_order,
        o.is_correct,
        false
    FROM submission_questions sq
    JOIN option o ON o.question_id = sq.question_id;
    
    
END $$
DELIMITER ;

-- dùng cursor loop

DELIMITER $$

CREATE PROCEDURE create_submission(
    IN p_quiz_id BIGINT,
    IN p_user_id BIGINT
)
BEGIN
    DECLARE v_submission_id BIGINT;
    DECLARE v_attempt_no INT;

    DECLARE done_question INT DEFAULT 0;
    DECLARE done_option INT DEFAULT 0;

    DECLARE v_question_id BIGINT;
    DECLARE v_question_text TEXT;
    DECLARE v_submission_question_id BIGINT;

    DECLARE v_option_id BIGINT;
    DECLARE v_option_text TEXT;
    DECLARE v_option_is_correct BOOLEAN;

    -- Tính lần làm mới
    SELECT COALESCE(MAX(attempt_no),0) + 1
    INTO v_attempt_no
    FROM submission
    WHERE quiz_id = p_quiz_id AND enrollment_id = p_user_id;

    -- Tạo submission
    INSERT INTO submission(quiz_id, enrollment_id, attempt_no, status, started_at, duration)
    VALUES(p_quiz_id, p_user_id, v_attempt_no, 'IN_PROGRESS', NOW(), 0);

    SET v_submission_id = LAST_INSERT_ID();

    -- Cursor cho câu hỏi (shuffle)
    DECLARE cur_question CURSOR FOR
        SELECT q.question_id, q.content
        FROM quiz_question qq
        JOIN question q ON qq.question_id = q.question_id
        WHERE qq.quiz_id = p_quiz_id
        ORDER BY RAND();

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done_question = 1;

    OPEN cur_question;
    SET @q_order = 1;

    question_loop: LOOP
        FETCH cur_question INTO v_question_id, v_question_text;
        IF done_question = 1 THEN
            LEAVE question_loop;
        END IF;

        -- Tạo submission_question
        INSERT INTO submission_question(submission_id, question_id, question_text, question_order, is_correct, score)
        VALUES(v_submission_id, v_question_id, v_question_text, @q_order, 0, 0);

        SET v_submission_question_id = LAST_INSERT_ID();
        SET @q_order = @q_order + 1;

        -- Cursor cho đáp án (shuffle)
        DECLARE cur_option CURSOR FOR
            SELECT option_id, content, is_correct
            FROM option
            WHERE question_id = v_question_id
            ORDER BY RAND();

        DECLARE CONTINUE HANDLER FOR NOT FOUND SET done_option = 1;

        OPEN cur_option;
        SET @o_order = 1;

        option_loop: LOOP
            FETCH cur_option INTO v_option_id, v_option_text, v_option_is_correct;
            IF done_option = 1 THEN
                LEAVE option_loop;
            END IF;

            -- Tạo submission_question_option
            INSERT INTO submission_question_option(
                submission_question_id, option_id, option_text, option_order, is_correct, is_chosen
            )
            VALUES(v_submission_question_id, v_option_id, v_option_text, @o_order, v_option_is_correct, 0);

            SET @o_order = @o_order + 1;
        END LOOP option_loop;

        CLOSE cur_option;
        SET done_option = 0; -- reset flag cho đáp án câu tiếp theo
    END LOOP question_loop;

    CLOSE cur_question;
END$$

DELIMITER ;



