package com.example.mentoringapplication.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "goal_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "category_name", nullable = false, unique = true)
    private String categoryName;
}