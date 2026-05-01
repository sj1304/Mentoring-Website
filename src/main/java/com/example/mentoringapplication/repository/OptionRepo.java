package com.example.mentoringapplication.repository;

import com.example.mentoringapplication.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OptionRepo extends JpaRepository<Option, Integer> {
    List<Option> findByQuestion_QuestionId(Integer questionId);
}