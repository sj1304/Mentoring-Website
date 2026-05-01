package com.example.mentoringapplication.service;

import com.example.mentoringapplication.entity.*;
import com.example.mentoringapplication.repository.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final MenteeRepo menteeRepo;
    private final MentorRepo mentorRepo;
    private final GoalProgressRepo goalProgressRepo;
    private final GoalsRepo goalsRepo;

    @Transactional
    public void saveStudentProgressUpdate(int goalId, int percentage, String feedback)
    {
        GoalProgress progress = goalProgressRepo.findByGoal_GoalId(goalId)
                .orElse(new GoalProgress());

        if (progress.getGoal() == null)
        {
            progress.setGoal(goalsRepo.findById(goalId)
                    .orElseThrow(() -> new RuntimeException("Goal not found")));

            progress.setStudent(progress.getGoal().getStudent());
        }

        progress.setCompletionPercentage(percentage);
        progress.setStudentFeedback(feedback);
        progress.setSubmittedAt(new Timestamp(System.currentTimeMillis()));
        goalProgressRepo.save(progress);
    }

    public List<Map<String, Object>> getStudentProgress(HttpSession session)
    {
        Integer mentorId = getMentorIdFromSession(session);

        List<Map<String, Object>> result = new ArrayList<>();

        if (mentorId == null)
            return result;

        List<Mentee> mentees = menteeRepo.findByMentor_Mid(mentorId);
        for (Mentee m : mentees)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("rollNo", m.getRollNo());
            map.put("firstName", m.getFirstName());
            map.put("lastName", m.getLastName());
            Double progress = goalProgressRepo.getCompletionPercentageByStudentId(m.getSid());
            map.put("progress", progress != null ? progress.intValue() : 0);
            result.add(map);
        }
        return result;
    }

    public List<Map<String, Object>> getMentorPerformance()
    {
        List<Map<String, Object>> result = new ArrayList<>();

        List<Mentor> mentors = mentorRepo.findAll();
        for (Mentor m : mentors)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("mentor", m.getFirstName() + " " + m.getLastName());
            Double avgProgress = goalProgressRepo.getAverageProgressByMentorId(m.getMid());
            map.put("completedGoals", avgProgress != null ? avgProgress.intValue() : 0);
            result.add(map);
        }

        result.sort((a, b) -> ((Integer) b.get("completedGoals")).compareTo((Integer) a.get("completedGoals")));

        return result;
    }



    public List<Map<String, Object>> getGoalCategories(HttpSession session)
    {
        Integer mentorId = getMentorIdFromSession(session);

        List<Map<String, Object>> result = new ArrayList<>();

        if (mentorId == null)
            return result;

        List<Object[]> categoryCounts = goalsRepo.countGoalsByCategoryForMentor(mentorId);
        for (Object[] row : categoryCounts)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("category", row[0] != null ? row[0].toString() : "Uncategorized");
            map.put("count", row[1] != null ? ((Number) row[1]).intValue() : 0);
            result.add(map);
        }

        return result;
    }


    public Map<String, Object> getGoalTimeline(HttpSession session)
    {
        Integer mentorId = getMentorIdFromSession(session);

        Map<String, Integer> createdPerMonth = new LinkedHashMap<>();

        Map<String, Integer> updatedPerMonth = new LinkedHashMap<>();
        if (mentorId != null)
        {
            List<Object[]> rows = goalsRepo.getGoalTimestampsByMentorId(mentorId);

            SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");

            for (Object[] row : rows)
            {
                Date c = convertToDate(row[0]);

                if (c != null)
                    createdPerMonth.merge(sdf.format(c), 1, Integer::sum);

                Date u = convertToDate(row[1]);
                if (u != null)
                    updatedPerMonth.merge(sdf.format(u), 1, Integer::sum);
            }
        }

        List<String> labels = new ArrayList<>(new TreeSet<>(createdPerMonth.keySet()));

        Map<String, Object> result = new HashMap<>();

        result.put("labels", labels);
        result.put("createdData", labels.stream().map(m -> createdPerMonth.getOrDefault(m, 0)).toList());
        result.put("updatedData", labels.stream().map(m -> updatedPerMonth.getOrDefault(m, 0)).toList());

        return result;
    }



    private Integer getMentorIdFromSession(HttpSession session)
    {
        Object mentorObj = session.getAttribute("mentorId");

        return (mentorObj != null) ? ((Number) mentorObj).intValue() : null;
    }


    private Date convertToDate(Object obj)
    {
        if (obj == null)
            return null;

        if (obj instanceof Date date)
            return date;

        if (obj instanceof Timestamp ts)
            return new Date(ts.getTime());

        if (obj instanceof LocalDateTime ldt)
            return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

        return null;
    }
}