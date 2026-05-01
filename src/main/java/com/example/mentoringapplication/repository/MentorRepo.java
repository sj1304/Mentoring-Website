package com.example.mentoringapplication.repository;

import com.example.mentoringapplication.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorRepo extends JpaRepository<Mentor, Integer> {

    Mentor findByUsername(String username);

    //  finds the MID of the mentor with the fewest students
    @Query(value = "SELECT m.mid FROM mentor m " +
            "LEFT JOIN mentee me ON m.mid = me.mentor_id " +
            "GROUP BY m.mid " +
            "ORDER BY COUNT(me.sid) ASC " +
            "LIMIT 1", nativeQuery = true)
    Integer findMentorWithLeastMentees();

    Mentor findByEmail(String email);
}