package com.dophuong.lms.learning_management_system.entity;

import com.dophuong.lms.learning_management_system.enums.ActionType;
import jakarta.persistence.*;

@Entity
@Table(name = "question_history")
public class QuestionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="question_id", nullable = false)
    private Question question;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActionType actionType;
}
