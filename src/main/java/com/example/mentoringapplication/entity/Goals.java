package com.example.mentoringapplication.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "goals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Integer goalId; // Changed to Integer

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Mentee student;

    @ManyToOne
    @JoinColumn(name = "cycle_id", nullable = false)
    private GoalCycle cycle;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private GoalTemplate template;

    @Column(name = "filled_text")
    private String filledText;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @org.hibernate.annotations.UpdateTimestamp
    private LocalDateTime updatedAt;
}