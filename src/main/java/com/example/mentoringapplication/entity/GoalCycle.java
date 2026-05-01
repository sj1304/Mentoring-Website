package com.example.mentoringapplication.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "goal_cycles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cycle_id")
    private Integer cycleId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Mentee student;

    @Column(name = "duration_days", nullable = false)
    private Integer durationDays; //changed to Integer from int

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private LocalDate endDate;
}