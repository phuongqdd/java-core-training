package com.dophuong.quiz_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quiz_question", uniqueConstraints = { @UniqueConstraint(columnNames = {"quiz_id", "question_id"}) })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_question_id")
    private Integer id;

    @Column(name = "quiz_id", nullable = false)
    private Long quiz_id;

    @Column(name = "question_id", nullable = false)
    private Long question_id;
}
