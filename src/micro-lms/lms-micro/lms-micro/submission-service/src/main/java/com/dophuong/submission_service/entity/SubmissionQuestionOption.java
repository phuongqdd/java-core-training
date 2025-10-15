package com.dophuong.submission_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "submission_question_option")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionQuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_option_id")
    private Long submissionOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_question_id", nullable = false)
    private SubmissionQuestion submissionQuestion;

    @Column(name = "option_id")
    private Long optionId;

    @Column(name = "option_text", nullable = false, columnDefinition = "TEXT")
    private String optionText;

    @Column(name = "option_order", nullable = false)
    private int optionOrder;

    @Column(name = "is_correct", nullable = false)
    private boolean correct;

    @Column(name = "is_chosen", nullable = false)
    private boolean chosen;
}
