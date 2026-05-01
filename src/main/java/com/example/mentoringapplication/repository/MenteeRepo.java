package com.example.mentoringapplication.repository;

import com.example.mentoringapplication.entity.Mentee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface MenteeRepo extends JpaRepository<Mentee, Integer> {
    // Find a mentee by username
    Mentee findByUsername(String username);

    // Optional: find by email
    Mentee findByEmail(String email);

    List<Mentee> findByMentor_Mid(int mid);

    int countByMentor_Mid(int mid);

}

