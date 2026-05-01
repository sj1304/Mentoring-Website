package com.example.mentoringapplication.repository;

import com.example.mentoringapplication.entity.PersonalityTest;
import com.example.mentoringapplication.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PersonalityTestRepo extends JpaRepository<PersonalityTest, Integer> {
    List<PersonalityTest> findByMentor(Mentor mentor);
}