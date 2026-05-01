package com.example.mentoringapplication.controller;

import com.example.mentoringapplication.entity.*;
import com.example.mentoringapplication.service.GoalsService;
import com.example.mentoringapplication.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor // Automatically generates a constructor for final fields
public class GoalsController {

    private final GoalsService goalService;
    private final AnalyticsService analyticsService;

    // DROP-DOWN & TEMPLATE DATA

    @GetMapping("/templates/{categoryId}")
    public List<GoalTemplate> getTemplatesByCategoryId(@PathVariable int categoryId) {
        return goalService.getTemplatesByCategoryId(categoryId);
    }

    // GOAL OPERATIONS

    @PostMapping
    public Goals createGoal(@RequestBody Goals goal) {
        // Sets status to "pending" by default for later tracking
        goal.setStatus("pending");
        return goalService.createGoal(goal);
    }

    @GetMapping("/active/{studentId}")
    public List<Goals> getActiveStudentGoals(@PathVariable int studentId) {
        Mentee student = new Mentee();
        student.setSid(studentId);
        // This should return goals where status is 'pending'
        return goalService.getStudentGoals(student);
    }

    //PROGRESS UPDATES (Used by My Goals page)
    @PostMapping("/progress/{goalId}")
    public String updateGoalProgress(
            @PathVariable int goalId,
            @RequestParam int percentage,
            @RequestParam(required = false) String feedback) {

        // Correctly calls the instance variable
        analyticsService.saveStudentProgressUpdate(goalId, percentage, feedback);
        return "Progress updated successfully to " + percentage + "%";
    }

    //CYCLE OPERATIONS

    @PostMapping("/cycle")
    public GoalCycle createGoalCycle(@RequestBody GoalCycle cycle) {
        return goalService.createGoalCycle(cycle);
    }
}