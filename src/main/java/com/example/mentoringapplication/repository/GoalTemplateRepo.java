package com.example.mentoringapplication.repository;

import com.example.mentoringapplication.entity.GoalTemplate;
import com.example.mentoringapplication.entity.GoalCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GoalTemplateRepo extends JpaRepository<GoalTemplate, Integer> {

    List<GoalTemplate> findByCategory(GoalCategory category);

}