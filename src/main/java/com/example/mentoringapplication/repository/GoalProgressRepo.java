package com.example.mentoringapplication.repository;

import com.example.mentoringapplication.entity.GoalProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface GoalProgressRepo extends JpaRepository<GoalProgress, Integer> {

    Optional<GoalProgress> findByGoal_GoalId(int goalId);


    @Query("SELECT AVG(g.completionPercentage) FROM GoalProgress g WHERE g.student.sid = :studentId")
    Double getCompletionPercentageByStudentId(@Param("studentId") int studentId);


    /** * FIXED: Calculates the average completion across ALL students for a specific mentor.
     * Uses native SQL to ensure the JOIN between goal_progress and mentee is accurate.
     **/
    @Query(value = "SELECT AVG(gp.completion_percentage) " +
            "FROM goal_progress gp " +
            "JOIN mentee m ON gp.student_id = m.sid " +
            "WHERE m.mentor_id = :mentorId", nativeQuery = true)
    Double getAverageProgressByMentorId(@Param("mentorId") int mentorId);
}