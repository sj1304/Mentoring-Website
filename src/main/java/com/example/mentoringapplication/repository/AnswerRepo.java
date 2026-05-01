package com.example.mentoringapplication.repository;

import com.example.mentoringapplication.entity.Answer;
import com.example.mentoringapplication.entity.Mentee;
import com.example.mentoringapplication.entity.PersonalityTest;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mentoringapplication.entity.Question;
import java.util.List;


public interface AnswerRepo extends JpaRepository<Answer, Integer> {
    List<Answer> findByStudentAndQuestion_Test(Mentee student, PersonalityTest test);
    List<Answer> findByQuestion(Question question);
}