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


