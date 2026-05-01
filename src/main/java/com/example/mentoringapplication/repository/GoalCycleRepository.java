package com.example.mentoringapplication.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mentoringapplication.entity.GoalCycle;

public interface GoalCycleRepository extends JpaRepository<GoalCycle, Integer> {
}
