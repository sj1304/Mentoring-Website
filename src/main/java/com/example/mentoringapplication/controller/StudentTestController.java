package com.example.mentoringapplication.controller;

import com.example.mentoringapplication.entity.*;
import com.example.mentoringapplication.repository.*;
import com.example.mentoringapplication.service.PersonalityTestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
public class StudentTestController {

    @Autowired private PersonalityTestService testService;
    @Autowired private MenteeRepo menteeRepo;

    // Get current student from SESSION (not hardcoded)
    private Mentee getCurrentStudent(HttpSession session) {
        Integer studentId = (Integer) session.getAttribute("studentId");
        if (studentId == null) return null;
        return menteeRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }


    // GET: View all assigned tests
    @GetMapping("/tests")
    public String viewTests(HttpSession session, Model model) {
        Mentee student = getCurrentStudent(session);
        if (student == null) return "redirect:/login";

        model.addAttribute("student", student);
        model.addAttribute("assignedTests",
                testService.getAssignedTestsForStudent(student));
        model.addAttribute("testService", testService);
        return "student_tests";
    }


    // GET: Attempt a test
    @GetMapping("/attempt-test/{testId}")
    public String attemptTest(@PathVariable Integer testId,
                              HttpSession session, Model model) {
        Mentee student = getCurrentStudent(session);
        if (student == null) return "redirect:/login";

        if (testService.hasAttempted(student, testId)) {
            return "redirect:/results?alreadyDone=true";
        }

        PersonalityTest test = testService.getTestById(testId);
        model.addAttribute("test", test);
        model.addAttribute("student", student);
        return "attempt_test";
    }


    // POST: Submit all answers
    @PostMapping("/submit-test/{testId}")
    public String submitTest(@PathVariable Integer testId,
                             @RequestParam Map<String, String> allParams,
                             HttpSession session) {
        Mentee student = getCurrentStudent(session);
        if (student == null) return "redirect:/login";

        Map<Integer, Integer> answerMap = new HashMap<>();
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (entry.getKey().startsWith("question_")) {
                Integer questionId = Integer.parseInt(
                        entry.getKey().replace("question_", ""));
                Integer optionId = Integer.parseInt(entry.getValue());
                answerMap.put(questionId, optionId);
            }
        }

        Option.PersonalityType result =
                testService.submitAnswers(student, testId, answerMap);

        return "redirect:/results?type=" + result.name() + "&testId=" + testId;
    }


    // GET: View personality result
    @GetMapping("/results")
    public String viewResults(@RequestParam(required = false) String type,
                              @RequestParam(required = false) Integer testId,
                              @RequestParam(required = false) Boolean alreadyDone,
                              HttpSession session, Model model) {
        Mentee student = getCurrentStudent(session);
        if (student == null) return "redirect:/login";

        model.addAttribute("student", student);
        model.addAttribute("personalityType", type);
        model.addAttribute("alreadyDone", alreadyDone);
        model.addAttribute("allResults",
                testService.getResultsForStudent(student));
        return "personality_result";
    }


    // GET: Student Dashboard — personality counts
    @GetMapping("/student-dashboard-data")  // ← changed from /Student_Dashboard
    public String studentDashboardData(HttpSession session, Model model) {
        Mentee student = getCurrentStudent(session);
        if (student == null) return "redirect:/login";

        List<PersonalityTest> assigned =
                testService.getAssignedTestsForStudent(student);
        List<PersonalityResult> results =
                testService.getResultsForStudent(student);

        model.addAttribute("student", student);
        model.addAttribute("assignedCount", assigned.size());
        model.addAttribute("completedCount", results.size());
        return "Student_Dashboard";
    }
}