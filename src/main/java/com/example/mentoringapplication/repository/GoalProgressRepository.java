package com.example.mentoringapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.mentoringapplication.entity.GoalProgress;
import org.springframework.data.repository.query.Param;

public interface GoalProgressRepository extends JpaRepository<GoalProgress, Integer> {

    @Query("SELECT AVG(g.completionPercentage) FROM GoalProgress g WHERE g.student.sid = :studentId")
    Double getCompletionPercentageByStudentId(@Param("studentId") int studentId);

    // Use g.student.mentorId since Mentee stores mentorId as a raw int
    @Query("SELECT COUNT(g) FROM GoalProgress g WHERE g.student.mentorId = :mentorId AND g.completionPercentage = 100")
    Integer countCompletedGoalsByMentorId(@Param("mentorId") int mentorId);
}