package com.example.mentoringapplication.controller;

import com.example.mentoringapplication.entity.*;
import com.example.mentoringapplication.repository.*;
import com.example.mentoringapplication.service.PersonalityTestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Controller
public class PersonalityTestController {

    @Autowired private PersonalityTestService testService;
    @Autowired private MentorRepo mentorRepo;
    @Autowired private MenteeRepo menteeRepo;


    // GET: Show Create Test Form
    @GetMapping("/create-test")
    public String showCreateTestForm(HttpSession session, Model model) {
        Integer mentorId = (Integer) session.getAttribute("mentorId");
        if (mentorId == null) return "redirect:/login";

        Mentor mentor = mentorRepo.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));
        model.addAttribute("mentor", mentor);
        return "create_test";
    }


    // POST: Save New Test
    @PostMapping("/create-test")
    public String createTest(@RequestParam String testName,
                             HttpSession session) {
        Integer mentorId = (Integer) session.getAttribute("mentorId");
        if (mentorId == null) return "redirect:/login";

        Mentor mentor = mentorRepo.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));
        PersonalityTest saved = testService.createTest(testName, mentor);
        return "redirect:/add-questions/" + saved.getTestId();
    }


    // GET: Show Add Questions Form
    @GetMapping("/add-questions/{testId}")
    public String showAddQuestionsForm(@PathVariable Integer testId,
                                       HttpSession session, Model model) {
        Integer mentorId = (Integer) session.getAttribute("mentorId");
        if (mentorId == null) return "redirect:/login";

        Mentor mentor = mentorRepo.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        PersonalityTest test = testService.getTestById(testId);
        model.addAttribute("mentor", mentor);
        model.addAttribute("test", test);
        return "add_questions";
    }

    // POST: Save Question + 4 Options
    @PostMapping("/add-questions/{testId}")
    public String addQuestion(@PathVariable Integer testId,
                              @RequestParam String questionText,
                              @RequestParam String option1Text,
                              @RequestParam String option1Type,
                              @RequestParam String option2Text,
                              @RequestParam String option2Type,
                              @RequestParam String option3Text,
                              @RequestParam String option3Type,
                              @RequestParam String option4Text,
                              @RequestParam String option4Type) {

        Question question = testService.addQuestion(testId, questionText);

        testService.addOption(question.getQuestionId(), option1Text,
                Option.PersonalityType.valueOf(option1Type));
        testService.addOption(question.getQuestionId(), option2Text,
                Option.PersonalityType.valueOf(option2Type));
        testService.addOption(question.getQuestionId(), option3Text,
                Option.PersonalityType.valueOf(option3Type));
        testService.addOption(question.getQuestionId(), option4Text,
                Option.PersonalityType.valueOf(option4Type));

        return "redirect:/add-questions/" + testId;
    }


    // GET: Show Assign Test Page
    @GetMapping("/assign-test/{testId}")
    public String showAssignPage(@PathVariable Integer testId,
                                 HttpSession session, Model model) {
        Integer mentorId = (Integer) session.getAttribute("mentorId");
        if (mentorId == null) return "redirect:/login";

        Mentor mentor = mentorRepo.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        PersonalityTest test = testService.getTestById(testId);
        List<Mentee> students = testService.getAllStudents();

        model.addAttribute("mentor", mentor);
        model.addAttribute("test", test);
        model.addAttribute("allStudents", students);
        return "assign_test";
    }


    // POST: Assign Test to Student
    @PostMapping("/assign-test/{testId}")
    public String assignTest(@PathVariable Integer testId,
                             @RequestParam Integer studentId) {
        Mentee student = menteeRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        testService.assignTestToStudent(testId, student);
        return "redirect:/assign-test/" + testId + "?success=true";
    }


    // GET: Manage Tests
    @GetMapping("/manage-tests")
    public String manageTests(HttpSession session, Model model) {
        Integer mentorId = (Integer) session.getAttribute("mentorId");
        if (mentorId == null) return "redirect:/login";

        Mentor mentor = mentorRepo.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        model.addAttribute("mentor", mentor);
        model.addAttribute("tests", testService.getTestsByMentor(mentor));
        return "manage_tests";
    }


    // GET: Evaluate — View Student Results
    @GetMapping("/evaluate")
    public String evaluatePage(HttpSession session, Model model) {
        Integer mentorId = (Integer) session.getAttribute("mentorId");
        if (mentorId == null) return "redirect:/login";

        Mentor mentor = mentorRepo.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        List<PersonalityTest> tests = testService.getTestsByMentor(mentor);
        List<PersonalityResult> allResults = testService.getAllResults();

        Map<PersonalityTest, List<PersonalityResult>> testResultsMap
                = new LinkedHashMap<>();
        for (PersonalityTest test : tests) {
            testResultsMap.put(test,
                    testService.getResultsByTest(test.getTestId()));
        }

        long extrovert  = allResults.stream()
                .filter(r -> r.getPersonalityType().name().equals("EXTROVERT")).count();
        long introvert  = allResults.stream()
                .filter(r -> r.getPersonalityType().name().equals("INTROVERT")).count();
        long analytical = allResults.stream()
                .filter(r -> r.getPersonalityType().name().equals("ANALYTICAL")).count();
        long amiable    = allResults.stream()
                .filter(r -> r.getPersonalityType().name().equals("AMIABLE")).count();
        long ambivert   = allResults.stream()
                .filter(r -> r.getPersonalityType().name().equals("AMBIVERT")).count();

        model.addAttribute("mentor", mentor);
        model.addAttribute("testResultsMap", testResultsMap);
        model.addAttribute("allResults", allResults);
        model.addAttribute("extrovert",  extrovert);
        model.addAttribute("introvert",  introvert);
        model.addAttribute("analytical", analytical);
        model.addAttribute("amiable",    amiable);
        model.addAttribute("ambivert",   ambivert);
        return "evaluate";
    }



    // POST: Delete a Test
    @PostMapping("/delete-test/{testId}")
    public String deleteTest(@PathVariable Integer testId) {
        testService.deleteTest(testId);
        return "redirect:/manage-tests";
    }
}