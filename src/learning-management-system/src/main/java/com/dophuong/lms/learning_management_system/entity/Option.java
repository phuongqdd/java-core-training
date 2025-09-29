package com.dophuong.lms.learning_management_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "`option`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    @Column(name = "is_correct", nullable = false)
    private boolean correct;
}
