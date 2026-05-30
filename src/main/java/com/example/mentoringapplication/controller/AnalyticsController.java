package com.example.mentoringapplication.controller;

import com.example.mentoringapplication.service.AnalyticsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    /**
     * Handles the main Reports and Analytics page.
     * This method provides both the chart data and the mentor profile metadata.
     */
    @GetMapping("/reports")
    public String reportsPage(HttpSession session, Model model) {
        // 1. Security Check: Ensure user is logged in
        Integer mentorId = (Integer) session.getAttribute("mentorId");
        if (mentorId == null) {
            return "redirect:/login";
        }

        // 2. Chart & Table Data for reports.js
        model.addAttribute("students", analyticsService.getStudentProgress(session));
        model.addAttribute("goalCategories", analyticsService.getGoalCategories(session));
        model.addAttribute("goalTimeline", analyticsService.getGoalTimeline(session));
        model.addAttribute("mentorPerformance", analyticsService.getMentorPerformance());

        // 3. Header Metadata: Pushes session data to the top  header
        // These must match the keys used in your reports.html th:text tags
        model.addAttribute("mentorName", session.getAttribute("mentorName"));
        model.addAttribute("mentorDept", session.getAttribute("mentorDept"));
        model.addAttribute("mentorLevel", session.getAttribute("mentorLevel"));
        model.addAttribute("mentorDesignation", session.getAttribute("mentorDesignation"));

        return "reports"; // Matches reports.html in your templates folder
    }

    /**
     * API Endpoint for real-time progress updates from the dashboard.
     */
    @GetMapping("/update-progress")
    @ResponseBody
    public ResponseEntity<String> updateProgress(
            @RequestParam int goalId,
            @RequestParam int percentage) {
        try {
            analyticsService.saveStudentProgressUpdate(goalId, percentage, "Initial progress set via dashboard");
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating progress: " + e.getMessage());
        }
    }
}
