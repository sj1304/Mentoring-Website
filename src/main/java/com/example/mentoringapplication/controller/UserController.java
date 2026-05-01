package com.example.mentoringapplication.controller;

import com.example.mentoringapplication.entity.Mentee;
import com.example.mentoringapplication.entity.Mentor;
import com.example.mentoringapplication.service.AnalyticsService;
import com.example.mentoringapplication.service.UserService;
import com.example.mentoringapplication.repository.GoalProgressRepo;
import com.example.mentoringapplication.repository.MenteeRepo;
import com.example.mentoringapplication.service.PersonalityTestService;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AnalyticsService analyticsService;
    private final GoalProgressRepo goalProgressRepo;
    private final MenteeRepo menteeRepo;
    private final PersonalityTestService personalityTestService;


    // HOME
    @GetMapping("/")
    public String indexPage() {
        return "index";
    }


    // REGISTER
    @PostMapping("/register")
    public String register(@RequestParam String role,
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam String username,
                           @RequestParam String password,
                           @RequestParam(required=false) String department,
                           @RequestParam(required=false) String designation,
                           @RequestParam(required=false) String experience,
                           @RequestParam(required=false) String teachingLevel,
                           @RequestParam(required=false) String rollNumber,
                           @RequestParam(required=false) String programmeLevel,
                           @RequestParam(required=false) String programme,
                           @RequestParam(required=false) String semester) {

        if (role.equalsIgnoreCase("mentor")) {
            Mentor m = new Mentor();
            m.setFirstName(firstName); m.setLastName(lastName);
            m.setEmail(email); m.setDepartment(department);
            m.setDesignation(designation); m.setExperience(experience);
            m.setTeachingLevel(teachingLevel); m.setUsername(username);
            m.setPassword(password);
            userService.registerMentor(m);
        } else {
            Mentee m = new Mentee();
            m.setFirstName(firstName); m.setLastName(lastName);
            m.setEmail(email); m.setRollNo(rollNumber);
            m.setProgrammeLevel(programmeLevel); m.setProgrammeName(programme);
            m.setSemester(semester); m.setUsername(username);
            m.setPassword(password);

            Mentor assignedMentor = userService.getMentorWithLeastMentees();
            if (assignedMentor != null) {
                m.setMentor(assignedMentor);
            }

            userService.registerMentee(m);
        }
        return "redirect:/login";
    }


    // LOGIN
    @PostMapping("/login")
    public String login(@RequestParam String role,
                        @RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {

        boolean success = userService.validateLogin(role, username, password);
        if (!success) return "redirect:/login?error";

        session.setAttribute("role", role);
        session.setAttribute("username", username);

        if (role.equalsIgnoreCase("mentor")) {
            Mentor mentor = userService.getMentorByUsername(username);
            session.setAttribute("mentorId", mentor.getMid());
            session.setAttribute("mentorName", mentor.getFirstName() + " " + mentor.getLastName());
            session.setAttribute("mentorDept", mentor.getDepartment());
            session.setAttribute("mentorDesignation", mentor.getDesignation());
            session.setAttribute("mentorLevel", mentor.getTeachingLevel());
            return "redirect:/mentor_dashboard";
        } else {
            Mentee mentee = userService.getMenteeByUsername(username);
            session.setAttribute("studentId", mentee.getSid());
            session.setAttribute("studentName", mentee.getFirstName() + " " + mentee.getLastName());
            session.setAttribute("studentRoll", mentee.getRollNo());
            session.setAttribute("studentProgramme", mentee.getProgrammeName());
            session.setAttribute("studentLevel", mentee.getProgrammeLevel());
            session.setAttribute("studentSemester", mentee.getSemester());
            return "redirect:/student_dashboard";
        }
    }


    // MENTOR DASHBOARD
    @GetMapping("/mentor_dashboard")
    public String mentorDashboard(HttpSession session, Model model) {
        Integer mentorId = (Integer) session.getAttribute("mentorId");
        if (mentorId == null) return "redirect:/login";

        List<Map<String, Object>> studentProgress = analyticsService.getStudentProgress(session);
        List<Map<String, Object>> mentorComparisonData = analyticsService.getMentorPerformance();

        Double currentAvg = goalProgressRepo.getAverageProgressByMentorId(mentorId);
        Map<String, Object> stats = new HashMap<>();
        stats.put("completedGoals", currentAvg != null ? currentAvg.intValue() : 0);
        stats.put("students", studentProgress);

        model.addAttribute("stats", stats);
        model.addAttribute("mentorPerformance", mentorComparisonData);
        model.addAttribute("mentorName", session.getAttribute("mentorName"));
        model.addAttribute("mentorDept", session.getAttribute("mentorDept"));
        model.addAttribute("mentorLevel", session.getAttribute("mentorLevel"));
        model.addAttribute("mentorDesignation", session.getAttribute("mentorDesignation"));

        return "mentor_dashboard";
    }


    // VIEW ASSIGNED STUDENTS
    @GetMapping("/students")
    public String viewStudents(HttpSession session, Model model) {
        Integer mentorId = (Integer) session.getAttribute("mentorId");
        if (mentorId == null) return "redirect:/login";

        List<Map<String, Object>> students = analyticsService.getStudentProgress(session);
        model.addAttribute("students", students);

        model.addAttribute("mentorName", session.getAttribute("mentorName"));
        model.addAttribute("mentorDept", session.getAttribute("mentorDept"));
        model.addAttribute("mentorLevel", session.getAttribute("mentorLevel"));
        model.addAttribute("mentorDesignation", session.getAttribute("mentorDesignation"));

        return "AssignedStudents";
    }


    // PROFILE VIEW
    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        if (role == null) return "redirect:/login";

        // Use .equalsIgnoreCase to avoid issues with "Mentor" vs "mentor"
        if (role.equalsIgnoreCase("mentor")) {
            Integer mid = (Integer) session.getAttribute("mentorId");
            Mentor mentor = userService.getMentorById(mid);

            if (mentor != null) {
                model.addAttribute("name", mentor.getFirstName() + " " + mentor.getLastName());
                model.addAttribute("email", mentor.getEmail());
                model.addAttribute("dept", mentor.getDepartment());
                model.addAttribute("desig", mentor.getDesignation());
                model.addAttribute("exp", mentor.getExperience());
                model.addAttribute("level", mentor.getTeachingLevel());
            }
        } else {
            Integer sid = (Integer) session.getAttribute("studentId");
            Mentee mentee = menteeRepo.findById(sid).orElse(null);

            if (mentee != null) {
                model.addAttribute("name", mentee.getFirstName() + " " + mentee.getLastName());
                model.addAttribute("email", mentee.getEmail());
                model.addAttribute("roll", mentee.getRollNo());
                model.addAttribute("prog", mentee.getProgrammeName());
                model.addAttribute("level", mentee.getProgrammeLevel());
                model.addAttribute("sem", mentee.getSemester());
            }
        }
        return "profile";
    }


    // STUDENT DASHBOARD
    @GetMapping("/student_dashboard")
    public String studentDashboard(HttpSession session, Model model) {
        Integer studentId = (Integer) session.getAttribute("studentId");
        if (studentId == null) return "redirect:/login";

        Mentee student = menteeRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        long assignedCount = personalityTestService.getAssignedTestsForStudent(student).size();
        long completedCount = personalityTestService.getResultsForStudent(student).size();

        model.addAttribute("studentName", session.getAttribute("studentName"));
        model.addAttribute("studentRoll", session.getAttribute("studentRoll"));
        model.addAttribute("studentProgramme", session.getAttribute("studentProgramme"));
        model.addAttribute("studentLevel", session.getAttribute("studentLevel"));
        model.addAttribute("studentSemester", session.getAttribute("studentSemester"));
        model.addAttribute("assignedCount", assignedCount);
        model.addAttribute("completedCount", completedCount);

        return "Student_Dashboard";
    }


    // PAGE MAPPINGS & LOGOUT
    @GetMapping("/goal-hub")
    public String goalHubPage(HttpSession session, Model model) {
        if (session.getAttribute("studentId") == null) return "redirect:/login";
        model.addAttribute("studentName", session.getAttribute("studentName"));
        model.addAttribute("studentRoll", session.getAttribute("studentRoll"));
        model.addAttribute("studentProgramme", session.getAttribute("studentProgramme"));
        return "Goal_Hub";
    }

    @GetMapping("/login") public String loginPage() { return "login"; }
    @GetMapping("/registration") public String registerPage() { return "registration"; }
    @GetMapping("/logout") public String logout(HttpSession session) { session.invalidate(); return "redirect:/login"; }
    @GetMapping("/quiz") public String quizPage() { return "quiz"; }
    @GetMapping("/my-goals") public String showMyGoalsPage() { return "MyGoals"; }
    @GetMapping("/about") public String aboutPage() { return "about"; }
    @GetMapping("/iqac")
    public String iqacPage() {
        return "iqac";
    }
}