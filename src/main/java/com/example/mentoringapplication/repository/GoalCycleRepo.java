package com.example.mentoringapplication.repository;

import com.example.mentoringapplication.entity.GoalCycle;
import com.example.mentoringapplication.entity.Mentee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GoalCycleRepo extends JpaRepository<GoalCycle, Integer> {

    List<GoalCycle> findByStudent(Mentee student);

}