package com.dophuong.quiz_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long course_id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "open_time", nullable = false)
    private LocalDateTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalDateTime closeTime;

    @Column(name = "time_limit", nullable = false)
    private int timeLimit;

    @Column(name = "attempts_allowed", nullable = false)
    private int attemptsAllowed = 1;

    @Column(name = "pass_mark")
    private float passMark;

    @Column(name = "allow_review", nullable = false)
    private boolean allowReview = true;

    @Column(name = "is_published", nullable = false)
    private boolean published = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "total")
    private int total;

    @Column(name = "pct_rl")
    private int pctRl = 30;

    @Column(name = "pct_un")
    private int pctUn = 30;

    @Column(name = "pct_ap")
    private int pctAp = 20;

    @Column(name = "pct_an")
    private int pctAn = 20;

}
