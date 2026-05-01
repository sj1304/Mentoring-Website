package com.example.mentoringapplication.repository;

import com.example.mentoringapplication.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepo extends JpaRepository<Question, Integer> {
    List<Question> findByTest_TestId(Integer testId);
}