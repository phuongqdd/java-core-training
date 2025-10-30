package com.dophuong.submission_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "submission_question")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_question_id")
    private Long submissionQuestionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "question_order", nullable = false)
    private int questionOrder;

    @Column(name = "is_correct")
    private boolean correct;

    @Column(name = "score")
    private Double score;

    @OneToMany(mappedBy = "submissionQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubmissionQuestionOption> options;
}
