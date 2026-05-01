package com.example.mentoringapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mentoringapplication.entity.Goal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Integer> {

    @Query("SELECT g.template.category.categoryName, COUNT(g) " +
            "FROM Goal g " +
            "WHERE g.student.mentorId = :mentorId " +
            "GROUP BY g.template.category.categoryName")
    List<Object[]> countGoalsByCategoryForMentor(@Param("mentorId") int mentorId);

    @Query("SELECT g.createdAt, g.updatedAt FROM Goal g WHERE g.student.mentorId = :mentorId")
    List<Object[]> getGoalTimestampsByMentorId(@Param("mentorId") int mentorId);
}