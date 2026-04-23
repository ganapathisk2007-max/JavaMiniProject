package com.classroom.cse_a_classroom.service;

import com.classroom.cse_a_classroom.dto.QuizDTO;
import com.classroom.cse_a_classroom.model.*;
import com.classroom.cse_a_classroom.repository.QuizRepository;
import com.classroom.cse_a_classroom.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.transaction.annotation.Transactional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private com.classroom.cse_a_classroom.repository.UserRepository userRepository;

    @Transactional
    public Quiz createQuiz(QuizDTO quizDTO, User teacher, Classroom classroom) {
        Quiz quiz = Quiz.builder()
                .title(quizDTO.getTitle())
                .topic(quizDTO.getTopic())
                .description(quizDTO.getDescription())
                .difficulty(quizDTO.getDifficulty())
                .timeLimit(quizDTO.getTimeLimit())
                .status(quizDTO.getStatus() != null ? quizDTO.getStatus() : "DRAFT")
                .password(quizDTO.getPassword())
                .teacher(teacher)
                .classroom(classroom)
                .build();

        List<Question> questions = quizDTO.getQuestions().stream().map(qDto -> 
            Question.builder()
                    .text(qDto.getText())
                    .options(qDto.getOptions())
                    .correctAnswer(qDto.getCorrectAnswer())
                    .quiz(quiz)
                    .build()
        ).collect(Collectors.toList());

        quiz.setQuestions(questions);
        return quizRepository.save(quiz);
    }

    public List<Quiz> getClassroomQuizzes(Classroom classroom) {
        return quizRepository.findByClassroom(classroom);
    }

    public List<Quiz> getPublishedQuizzes(Classroom classroom) {
        return quizRepository.findByClassroomAndStatus(classroom, "PUBLISHED");
    }

    public Quiz publishQuiz(Long id) {
        Quiz quiz = getQuizById(id);
        quiz.setStatus("PUBLISHED");
        return quizRepository.save(quiz);
    }

    public Quiz unpublishQuiz(Long id) {
        Quiz quiz = getQuizById(id);
        quiz.setStatus("DRAFT");
        return quizRepository.save(quiz);
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    public boolean validatePassword(Long quizId, String password) {
        Quiz quiz = getQuizById(quizId);
        return quiz.getPassword().equals(password);
    }

    public Submission submitQuiz(Long quizId, User student, Map<Long, String> answers, Integer timeTaken) {
        Quiz quiz = getQuizById(quizId);
        if (submissionRepository.existsByStudentAndQuiz(student, quiz)) {
            throw new RuntimeException("You have already submitted this quiz");
        }

        int score = 0;
        int correct = 0;
        int wrong = 0;
        for (Question question : quiz.getQuestions()) {
            String studentAnswer = answers.get(question.getId());
            if (studentAnswer != null && studentAnswer.equals(question.getCorrectAnswer())) {
                score++;
                correct++;
            } else {
                wrong++;
            }
        }

        double percentage = (double) correct / quiz.getQuestions().size() * 100;
        
        String studentAnswersJson = "";
        try {
            studentAnswersJson = new ObjectMapper().writeValueAsString(answers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Submission submission = Submission.builder()
                .student(student)
                .quiz(quiz)
                .score(score)
                .totalQuestions(quiz.getQuestions().size())
                .correctAnswers(correct)
                .wrongAnswers(wrong)
                .percentage(percentage)
                .timeTaken(timeTaken != null ? timeTaken : 0)
                .studentAnswers(studentAnswersJson)
                .build();

        // Gamification Logic
        int xpGained = score * 100;
        student.setXp(student.getXp() + xpGained);
        student.setLevel((student.getXp() / 1000) + 1);
        
        if(student.getLevel() >= 10) student.setTitle("Quiz Grandmaster");
        else if(student.getLevel() >= 5) student.setTitle("Expert Scholar");
        else if(student.getLevel() >= 2) student.setTitle("Rising Star");
        else student.setTitle("Novice");
        
        userRepository.save(student);

        return submissionRepository.save(submission);
    }

    public List<Submission> getLeaderboard(Long quizId) {
        Quiz quiz = getQuizById(quizId);
        return submissionRepository.findByQuiz(quiz).stream()
                .sorted((s1, s2) -> {
                    if (s2.getScore() != s1.getScore()) {
                        return s2.getScore() - s1.getScore();
                    }
                    return s1.getTimeTaken() - s2.getTimeTaken();
                })
                .collect(Collectors.toList());
    }

    public List<Submission> getQuizSubmissions(Long quizId) {
        return submissionRepository.findByQuiz(getQuizById(quizId));
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    public Map<String, Object> getClassroomPerformance(Classroom classroom) {
        List<User> students = classroom.getMembers();
        List<Quiz> quizzes = quizRepository.findByClassroom(classroom);
        
        List<Map<String, Object>> studentStats = students.stream().map(student -> {
            Map<String, Object> stats = new java.util.HashMap<>();
            stats.put("name", student.getName());
            stats.put("email", student.getEmail());
            
            // Filter submissions to only those belonging to quizzes in THIS classroom
            List<Submission> submissions = submissionRepository.findByStudent(student).stream()
                .filter(s -> quizzes.contains(s.getQuiz()))
                .collect(Collectors.toList());
            
            stats.put("submissions", submissions);
            stats.put("totalQuizzes", quizzes.size());
            stats.put("completed", submissions.size());
            return stats;
        }).collect(Collectors.toList());

        Map<String, Object> response = new java.util.HashMap<>();
        response.put("className", classroom.getName());
        response.put("students", studentStats);
        response.put("totalQuizzes", quizzes.size());
        return response;
    }
}
