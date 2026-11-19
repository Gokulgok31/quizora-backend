package io.quizora.backend.service.impl;

import io.quizora.backend.dto.QuestionDto;
import io.quizora.backend.dto.QuizAttemptRequestDto;
import io.quizora.backend.dto.QuizAttemptResponseDto;
import io.quizora.backend.entity.*;
import io.quizora.backend.repository.QuestionRepository;
import io.quizora.backend.repository.QuizAttemptRepository;
import io.quizora.backend.repository.QuizRepository;
import io.quizora.backend.repository.StudentRepository;
import io.quizora.backend.service.QuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuizAttemptServiceImpl implements QuizAttemptService {

    @Autowired
    QuizAttemptRepository quizAttemptRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    QuizRepository quizRepository;
    @Autowired
    QuestionRepository questionRepository;


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

    @Override
    public List<QuestionDto> generateQuestionsForQuizAttempt(Integer attemptId) {

        QuizAttempt quizAttempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Quiz attempt Id not found"));
        if(!quizAttempt.getStatus().equals(AttemptStatus.IN_PROGRESS)) {
            throw new RuntimeException("Quiz was Not Attempted Before");
        }
        Quiz quiz = quizAttempt.getQuiz();
        Exam exam = quiz.getExam();
        List<Question> questionsList = questionRepository.findByExam_ExamCode(exam.getExamCode());
        Integer configuredQuestions = quiz.getNumberOfQuestions();
        Integer totalQuestions = questionsList.size();
        if(totalQuestions < configuredQuestions) {
            throw new RuntimeException("Not enough questions present");
        }
        questionsList = questionsList.subList(0, configuredQuestions);
        List<QuestionDto> displayQuestions = getQuestionDtos(quiz, questionsList);
        return displayQuestions;
    }

    private static List<QuestionDto> getQuestionDtos(Quiz quiz, List<Question> questionsList) {
        List<QuestionDto> displayQuestions = new ArrayList<>();
        for(Question question: questionsList) {
            QuestionDto questionDto = new QuestionDto();
            questionDto.setId(question.getId());
            questionDto.setQuestionTitle(question.getQuestionTitle());
            questionDto.setOption1(question.getOption1());
            questionDto.setOption2(question.getOption2());
            questionDto.setOption3(question.getOption3());
            questionDto.setOption4(question.getOption4());
            displayQuestions.add(questionDto);
        }
        return displayQuestions;
    }
}
