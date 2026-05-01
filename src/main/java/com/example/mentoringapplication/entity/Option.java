package com.example.mentoringapplication.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "options")
public class Option {

    public enum PersonalityType {
        EXTROVERT, INTROVERT, ANALYTICAL, AMIABLE, AMBIVERT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer optionId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String optionText;

    @Enumerated(EnumType.STRING)
    private PersonalityType personalityType;

    // Getters and Setters
    public Integer getOptionId() { return optionId; }
    public void setOptionId(Integer optionId) { this.optionId = optionId; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }
    public PersonalityType getPersonalityType() { return personalityType; }
    public void setPersonalityType(PersonalityType personalityType) { this.personalityType = personalityType; }
}