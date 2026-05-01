package com.example.mentoringapplication.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="mentor")
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mid;

    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String designation;
    private String experience;
    private String teachingLevel;
    private String username;
    private String password;
}

