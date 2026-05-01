package com.example.mentoringapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mentoringapplication.entity.GoalCategory;

public interface GoalCategoryRepository extends JpaRepository<GoalCategory, Integer> {
}