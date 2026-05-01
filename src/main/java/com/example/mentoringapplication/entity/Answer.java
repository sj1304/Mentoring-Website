package com.example.mentoringapplication.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Mentee student;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "selected_option")
    private Option selectedOption;

    // Getters and Setters
    public Integer getAnswerId() { return answerId; }
    public void setAnswerId(Integer answerId) { this.answerId = answerId; }
    public Mentee getStudent() { return student; }
    public void setStudent(Mentee student) { this.student = student; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    public Option getSelectedOption() { return selectedOption; }
    public void setSelectedOption(Option selectedOption) { this.selectedOption = selectedOption; }
}