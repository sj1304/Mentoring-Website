package com.example.mentoringapplication.service;

import com.example.mentoringapplication.entity.*;
import com.example.mentoringapplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class PersonalityTestService {

    @Autowired private PersonalityTestRepo testRepo;
    @Autowired private QuestionRepo questionRepo;
    @Autowired private OptionRepo optionRepo;
    @Autowired private AnswerRepo answerRepo;
    @Autowired private PersonalityResultRepo resultRepo;
    @Autowired private MenteeRepo menteeRepo;

    // ---- MENTOR SIDE ----

    public PersonalityTest createTest(String testName, Mentor mentor) {
        PersonalityTest test = new PersonalityTest();
        test.setTestName(testName);
        test.setMentor(mentor);
        return testRepo.save(test);
    }

    public Question addQuestion(Integer testId, String questionText) {
        PersonalityTest test = testRepo.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));
        Question q = new Question();
        q.setTest(test);
        q.setQuestionText(questionText);
        return questionRepo.save(q);
    }

    public Option addOption(Integer questionId, String optionText, Option.PersonalityType type) {
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        Option option = new Option();
        option.setQuestion(question);
        option.setOptionText(optionText);
        option.setPersonalityType(type);
        return optionRepo.save(option);
    }

    public void assignTestToStudent(Integer testId, Mentee student) {
        PersonalityTest test = testRepo.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));
        test.getAssignedStudents().add(student);
        testRepo.save(test);
    }

    @Transactional
    public void deleteTest(Integer testId) {
        PersonalityTest test = testRepo.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        // Step 1: Delete answers related to this test's questions
        for (Question q : test.getQuestions()) {
            List<Answer> answers = answerRepo.findByQuestion(q);
            answerRepo.deleteAll(answers);
        }

        // Step 2: Delete personality results for this test
        List<PersonalityResult> results = resultRepo.findByTest(test);
        resultRepo.deleteAll(results);

        // Step 3: Clear assigned students (ManyToMany)
        test.getAssignedStudents().clear();
        testRepo.save(test);

        // Step 4: Delete test (cascade deletes questions + options)
        testRepo.delete(test);
    }

    // Get all results for all students (for mentor reports)
    public List<PersonalityResult> getAllResults() {
        return resultRepo.findAll();
    }

    // Get results by test
    public List<PersonalityResult> getResultsByTest(Integer testId) {
        PersonalityTest test = testRepo.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));
        return resultRepo.findByTest(test);
    }
    // ---- STUDENT SIDE (Day 2 will complete this) ----

    public List<PersonalityTest> getTestsForStudent(Mentee student) {
        // Will be fully implemented Day 2
        return testRepo.findAll();
    }



    public Option.PersonalityType calculateResult(Mentee student, Integer testId) {
        // Counting logic in Day 2
        return null;
    }

    // ---- Add this method — Get tests assigned to a specific student ----
    public List<PersonalityTest> getAssignedTestsForStudent(Mentee student) {
        return testRepo.findAll().stream()
                .filter(t -> t.getAssignedStudents().contains(student))
                .collect(java.util.stream.Collectors.toList());
    }

    // ---- Add this method — Get all students (for assign dropdown) ----
    public List<Mentee> getAllStudents() {
        return menteeRepo.findAll();
    }

    // ---- Add this method — Get all tests by mentor ----
    public List<PersonalityTest> getTestsByMentor(Mentor mentor) {
        return testRepo.findByMentor(mentor);
    }

    // ---- Add this method — Get single test ----
    public PersonalityTest getTestById(Integer testId) {
        return testRepo.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));
    }

    // In PersonalityTestService.java


    // ─────────────────────────────────────────
// Submit all answers + calculate + save result
// ─────────────────────────────────────────
    public Option.PersonalityType submitAnswers(Mentee student,
                                                Integer testId,
                                                Map<Integer, Integer> questionToOptionMap) {

        PersonalityTest test = testRepo.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        // Step 1: Save each answer
        for (Map.Entry<Integer, Integer> entry : questionToOptionMap.entrySet()) {
            Integer questionId = entry.getKey();
            Integer optionId   = entry.getValue();

            Question question = questionRepo.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found"));
            Option option = optionRepo.findById(optionId)
                    .orElseThrow(() -> new RuntimeException("Option not found"));

            Answer answer = new Answer();
            answer.setStudent(student);
            answer.setQuestion(question);
            answer.setSelectedOption(option);
            answerRepo.save(answer);
        }

        // Step 2: Count personality type frequency
        List<Answer> allAnswers = answerRepo
                .findByStudentAndQuestion_Test(student, test);

        Map<Option.PersonalityType, Integer> countMap = new HashMap<>();
        for (Answer a : allAnswers) {
            Option.PersonalityType type = a.getSelectedOption().getPersonalityType();
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        // Step 3: Find high count = final personality type
        Option.PersonalityType finalType = countMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Option.PersonalityType.AMBIVERT);

        // Step 4: Save result
        PersonalityResult result = new PersonalityResult();
        result.setStudent(student);
        result.setTest(test);
        result.setPersonalityType(finalType);
        resultRepo.save(result);

        return finalType;
    }

    // ─────────────────────────────────────────
// Get all results for a student
// ─────────────────────────────────────────
    public List<PersonalityResult> getResultsForStudent(Mentee student) {
        return resultRepo.findByStudent(student);
    }

    // ─────────────────────────────────────────
// Check if student already attempted a test
// ─────────────────────────────────────────
    public boolean hasAttempted(Mentee student, Integer testId) {
        return resultRepo.findByStudentAndTest_TestId(student, testId).isPresent();
    }
}