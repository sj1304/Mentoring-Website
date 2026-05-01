package com.example.mentoringapplication.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "goals")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Integer goalId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Mentee student;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private GoalTemplate template;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // getters and setters
}
