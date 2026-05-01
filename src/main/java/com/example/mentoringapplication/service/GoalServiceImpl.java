package com.example.mentoringapplication.service;

import com.example.mentoringapplication.entity.*;
import com.example.mentoringapplication.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalsService {

    private final GoalsRepo goalRepository;
    private final GoalCycleRepo goalCycleRepository;
    private final GoalCategoryRepo goalCategoryRepository;
    private final GoalTemplateRepo goalTemplateRepository;
    private final GoalProgressRepo goalProgressRepository;

    @Override
    @Transactional
    public Goals createGoal(Goals goal) {
        // 1. Save the main goal record
        Goals savedGoal = goalRepository.save(goal);

        // 2. Initialize progress at 0% so it immediately appears on the Mentor Dashboard
        GoalProgress progress = new GoalProgress();
        progress.setGoal(savedGoal);
        progress.setStudent(savedGoal.getStudent());
        progress.setCompletionPercentage(0);
        progress.setSubmittedAt(new Timestamp(System.currentTimeMillis()));

        goalProgressRepository.save(progress);

        return savedGoal;
    }

    /**
     * Call this method when the student updates their own progress
     * from their dashboard/slider.
     */
    @Transactional
    public void updateStudentSelfProgress(int goalId, int percentage, String feedback)
    {
        GoalProgress progress = goalProgressRepository.findByGoal_GoalId(goalId)
                .orElse(new GoalProgress());

        if (progress.getGoal() == null)
        {
            Goals goal = goalRepository.findById(goalId)
                    .orElseThrow(() -> new RuntimeException("Goal not found"));

            progress.setGoal(goal);
            progress.setStudent(goal.getStudent());
        }

        progress.setCompletionPercentage(percentage);
        progress.setStudentFeedback(feedback);
        progress.setSubmittedAt(new Timestamp(System.currentTimeMillis()));

        goalProgressRepository.save(progress);
    }

    @Override
    public List<Goals> getStudentGoals(Mentee student)
    {
        return goalRepository.findByStudent(student);
    }

    @Override
    public List<Goals> getCompletedGoals(Mentee student)
    {
        return goalRepository.findByStudentAndStatus(student, "completed");
    }

    @Override
    public Goals markGoalCompleted(int goalId)
    {
        Goals goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        goal.setStatus("completed");

        return goalRepository.save(goal);
    }


    @Override
    public GoalCycle createGoalCycle(GoalCycle cycle)
    {
        return goalCycleRepository.save(cycle);
    }


    @Override
    public List<GoalCycle> getStudentGoalCycles(Mentee student)
    {
        return goalCycleRepository.findByStudent(student);
    }


    @Override
    public List<GoalCategory> getAllCategories()
    {
        return goalCategoryRepository.findAll();
    }



    @Override
    public List<GoalTemplate> getTemplatesByCategoryId(int categoryId)
    {
        GoalCategory category = new GoalCategory();
        category.setCategoryId(categoryId);

        return goalTemplateRepository.findByCategory(category);
    }

}