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


