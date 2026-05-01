package com.example.mentoringapplication.entity;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name="mentee")
public class Mentee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sid;

    private String firstName;
    private String lastName;
    private String email;
    private String rollNo;
    private String programmeLevel;
    private String programmeName;
    private String semester;
    private String username;
    private String password;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;
}

