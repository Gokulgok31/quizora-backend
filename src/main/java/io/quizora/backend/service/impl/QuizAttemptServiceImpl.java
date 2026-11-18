package io.quizora.backend.service.impl;

import io.quizora.backend.dto.QuizAttemptRequestDto;
import io.quizora.backend.dto.QuizAttemptResponseDto;
import io.quizora.backend.entity.AttemptStatus;
import io.quizora.backend.entity.Quiz;
import io.quizora.backend.entity.QuizAttempt;
import io.quizora.backend.entity.Student;
import io.quizora.backend.repository.QuizAttemptRepository;
import io.quizora.backend.repository.QuizRepository;
import io.quizora.backend.repository.StudentRepository;
import io.quizora.backend.service.QuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class QuizAttemptServiceImpl implements QuizAttemptService {

    @Autowired
    QuizAttemptRepository quizAttemptRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    QuizRepository quizRepository;


    @Override
    public QuizAttemptResponseDto startQuiz(
            QuizAttemptRequestDto quizAttemptRequestDto) {

        Student student = studentRepository
                .findByRollNumber(quizAttemptRequestDto.getRollNumber());

        if (student == null) {
            throw new RuntimeException("Please register as a student first");
        }

        if (!student.getStudentName()
                .equalsIgnoreCase(quizAttemptRequestDto.getStudentName())) {

            throw new RuntimeException("Enter correct student name");
        }

        Integer quizId = quizAttemptRequestDto.getQuizId();

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() ->
                        new RuntimeException("Quiz not found with ID: " + quizId));

        boolean alreadyAttempted =
                quizAttemptRepository.existsByStudent_IdAndQuiz_Id(
                        student.getId(), quizId);

        if (alreadyAttempted) {
            throw new RuntimeException("Already attempted this quiz");
        }

        QuizAttempt quizAttempt = new QuizAttempt();

        quizAttempt.setStudent(student);
        quizAttempt.setQuiz(quiz);
        quizAttempt.setStartedAt(LocalDateTime.now());
        quizAttempt.setStatus(AttemptStatus.IN_PROGRESS);

        QuizAttempt savedAttempt =
                quizAttemptRepository.save(quizAttempt);

        QuizAttemptResponseDto response = new QuizAttemptResponseDto();

        response.setAttemptId(savedAttempt.getId());
        response.setRollNumber(student.getRollNumber());
        response.setStudentName(student.getStudentName());
        response.setQuizId(quiz.getId());
        response.setStatus(savedAttempt.getStatus().name());
        response.setStartedAt(savedAttempt.getStartedAt());

        return response;
    }
}
