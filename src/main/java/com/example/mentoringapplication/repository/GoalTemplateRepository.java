package com.example.mentoringapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mentoringapplication.entity.GoalTemplate;

public interface GoalTemplateRepository extends JpaRepository<GoalTemplate, Integer> {
}
