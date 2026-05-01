package com.example.mentoringapplication.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "goal_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Integer templateId; // Changed to Integer

    @ManyToOne
    @JoinColumn(name = "category_id")
    private GoalCategory category;

    @Column(name = "template_text")
    private String templateText;
}