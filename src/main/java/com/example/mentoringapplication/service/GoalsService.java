package com.example.mentoringapplication.service;

import com.example.mentoringapplication.entity.*;
import java.util.List;

public interface GoalsService {

    Goals createGoal(Goals goal);

    List<Goals> getStudentGoals(Mentee student);

    List<Goals> getCompletedGoals(Mentee student);

    Goals markGoalCompleted(int goalId);

    GoalCycle createGoalCycle(GoalCycle cycle);

    List<GoalCycle> getStudentGoalCycles(Mentee student);

    // New methods for dropdown logic
    List<GoalCategory> getAllCategories();

    List<GoalTemplate> getTemplatesByCategoryId(int categoryId);
}