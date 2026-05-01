package com.example.mentoringapplication.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import com.example.mentoringapplication.entity.PersonalityTest;
import java.util.List;

@Entity
@Table(name = "tests")
public class PersonalityTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer testId;

    private String testName;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)  // ← add orphanRemoval
    private List<Question> questions = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "test_assignments",
            joinColumns = @JoinColumn(name = "test_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Mentee> assignedStudents = new ArrayList<>();  // ← initialized

    // Getters and Setters
    public Integer getTestId() { return testId; }
    public void setTestId(Integer testId) { this.testId = testId; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public Mentor getMentor() { return mentor; }
    public void setMentor(Mentor mentor) { this.mentor = mentor; }
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    public List<Mentee> getAssignedStudents() { return assignedStudents; }
    public void setAssignedStudents(List<Mentee> assignedStudents) { this.assignedStudents = assignedStudents; }
}