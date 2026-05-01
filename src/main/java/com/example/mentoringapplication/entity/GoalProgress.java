package com.example.mentoringapplication.entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Table(name = "goal_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private int progressId;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goals goal;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Mentee student;

    @Column(name = "completion_percentage")
    private Integer completionPercentage;

    @Column(name = "student_feedback")
    private String studentFeedback;

    @Column(name = "submitted_at")
    private Timestamp submittedAt;
}