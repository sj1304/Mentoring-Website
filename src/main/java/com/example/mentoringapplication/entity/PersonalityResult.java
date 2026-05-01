package com.example.mentoringapplication.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "results")
public class PersonalityResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resultId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Mentee student;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private PersonalityTest test;

    @Enumerated(EnumType.STRING)
    private Option.PersonalityType personalityType;

    // Getters and Setters
    public Integer getResultId() { return resultId; }
    public void setResultId(Integer resultId) { this.resultId = resultId; }
    public Mentee getStudent() { return student; }
    public void setStudent(Mentee student) { this.student = student; }
    public PersonalityTest getTest() { return test; }
    public void setTest(PersonalityTest test) { this.test = test; }
    public Option.PersonalityType getPersonalityType() { return personalityType; }
    public void setPersonalityType(Option.PersonalityType personalityType) { this.personalityType = personalityType; }
}