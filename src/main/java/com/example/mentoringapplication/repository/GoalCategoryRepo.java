package com.example.mentoringapplication.repository;

import com.example.mentoringapplication.entity.GoalCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GoalCategoryRepo extends JpaRepository<GoalCategory, Integer> {

    Optional<GoalCategory> findByCategoryName(String categoryName);

}