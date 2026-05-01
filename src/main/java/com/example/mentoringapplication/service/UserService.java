package com.example.mentoringapplication.service;

import com.example.mentoringapplication.entity.Mentee;
import com.example.mentoringapplication.entity.Mentor;
import com.example.mentoringapplication.repository.MenteeRepo;
import com.example.mentoringapplication.repository.MentorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MentorRepo mentorRepo;
    private final MenteeRepo menteeRepo;


    // REGISTER
    public void registerMentor(Mentor mentor) {
        mentorRepo.save(mentor);
    }

    public void registerMentee(Mentee mentee) {
        menteeRepo.save(mentee);
    }


    // AUTO ASSIGN MENTOR — from goal module
    public Mentor getMentorWithLeastMentees()
    {
        Integer mentorId = mentorRepo.findMentorWithLeastMentees();

        if (mentorId == null)
        {
            return null; // No mentors registered yet
        }

        return mentorRepo.findById(mentorId).orElse(null);
    }



    // LOGIN
    public boolean validateLogin(String role, String username, String password) {
        if (role.equalsIgnoreCase("mentor")) {
            Mentor m = mentorRepo.findByUsername(username);
            return m != null && m.getPassword().equals(password);
        } else {
            Mentee m = menteeRepo.findByUsername(username);
            return m != null && m.getPassword().equals(password);
        }
    }


    // GET BY USERNAME — from goal module

    public Mentor getMentorByUsername(String username) {
        return mentorRepo.findByUsername(username);
    }

    public Mentee getMenteeByUsername(String username) {
        return menteeRepo.findByUsername(username);
    }

    public Mentor getMentorById(Integer id) {
        return mentorRepo.findById(id).orElse(null);
    }
}