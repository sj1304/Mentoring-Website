package com.example.mentoringapplication.repository;

import com.example.mentoringapplication.entity.Goals;
import com.example.mentoringapplication.entity.Mentee;
import com.example.mentoringapplication.entity.GoalCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface GoalsRepo extends JpaRepository<Goals, Integer> {

    // Student Operations
    List<Goals> findByStudent(Mentee student);
    List<Goals> findByCycle(GoalCycle cycle);
    List<Goals> findByStudentAndStatus(Mentee student, String status);



    // Analytics Queries
    @Query("SELECT g.template.category.categoryName, COUNT(g) " +
            "FROM Goals g " +
            "WHERE g.student.mentor.mid = :mentorId " +
            "GROUP BY g.template.category.categoryName")
    List<Object[]> countGoalsByCategoryForMentor(@Param("mentorId") int mentorId);

    @Query("SELECT g.createdAt, g.updatedAt FROM Goals g WHERE g.student.mentor.mid = :mentorId")
    List<Object[]> getGoalTimestampsByMentorId(@Param("mentorId") int mentorId);
}

