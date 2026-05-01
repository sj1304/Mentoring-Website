package com.example.mentoringapplication.repository;

import com.example.mentoringapplication.entity.PersonalityResult;
import com.example.mentoringapplication.entity.Mentee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import com.example.mentoringapplication.entity.PersonalityTest;


public interface PersonalityResultRepo extends JpaRepository<PersonalityResult, Integer> {
    Optional<PersonalityResult> findByStudentAndTest_TestId(Mentee student, Integer testId);
    List<PersonalityResult> findByStudent(Mentee student);
    List<PersonalityResult> findByTest(PersonalityTest test);
}