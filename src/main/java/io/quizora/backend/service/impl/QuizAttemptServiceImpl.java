package io.quizora.backend.service.impl;

import io.quizora.backend.dto.*;
import io.quizora.backend.entity.*;
import io.quizora.backend.repository.*;
import io.quizora.backend.service.QuizAttemptService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
    @Autowired
    StudentAnswerRepository studentAnswerRepository;


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

    @Override
    public boolean validateQuizAttempt(QuizAttempt quizAttempt) {
        if(AttemptStatus.IN_PROGRESS.equals(quizAttempt.getStatus())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean validateTimeLimit(QuizAttempt quizAttempt) {

        Quiz quiz = quizAttempt.getQuiz();

        LocalDateTime startTime = quizAttempt.getStartedAt();
        LocalDateTime endTime = startTime.plusMinutes(quiz.getDurationInMinutes());
        LocalDateTime currentTime = LocalDateTime.now();

        return !currentTime.isAfter(endTime);
    }

    @Override
    public boolean validateSubmittedQuestionId(QuizAttempt quizAttempt, StudentAnswerRequestDto studentAnswerRequestDto) {
        Exam exam = quizAttempt.getQuiz().getExam();
        List<Question> questions = questionRepository.findByExam_ExamCode(exam.getExamCode());
        Quiz quiz = quizAttempt.getQuiz();
        if(questions.size() < quiz.getNumberOfQuestions()) {
            throw new RuntimeException("Not enough questions found");
        }
        boolean validation = questions.stream()
                .limit(quiz.getNumberOfQuestions())
                .anyMatch(question -> question.getId().equals(studentAnswerRequestDto.getQuestionId()));
        return validation;
    }

    @Transactional
    @Override
    public void submitAnswers(AnswerSubmissionRequestDto requestDto) {

        QuizAttempt quizAttempt = quizAttemptRepository.findById(requestDto.getAttemptId())
                .orElseThrow(() -> new RuntimeException("Quiz Attempt not found"));

        if (!AttemptStatus.IN_PROGRESS.equals(quizAttempt.getStatus())) {
            throw new RuntimeException("Quiz attempt is not in progress");
        }
        if (!validateTimeLimit(quizAttempt)) {
            throw new RuntimeException("Time exceeded, failed to submit answers");
        }
        List<StudentAnswerRequestDto> submittedAnswers = requestDto.getSubmittedAnswers();

        if (submittedAnswers == null) {
            throw new RuntimeException("Submitted answers cannot be null");
        }
        Set<Integer> questionIds = new HashSet<>();

        for (StudentAnswerRequestDto answer : submittedAnswers) {

            if (answer.getQuestionId() == null) {
                throw new RuntimeException("Question ID cannot be null");
            }

            if (!questionIds.add(answer.getQuestionId())) {
                throw new RuntimeException("Duplicate question ID submitted");
            }

            if (!validateSubmittedQuestionId(quizAttempt, answer)) {
                throw new RuntimeException(
                        "Invalid question ID: " + answer.getQuestionId());
            }
        }

        // 6. Fetch questions and prepare student answers
        List<StudentAnswer> studentAnswers = new ArrayList<>();
        int marks = 0;

        for (StudentAnswerRequestDto answer : submittedAnswers) {

            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new RuntimeException(
                            "Question not found: " + answer.getQuestionId()));

            StudentAnswer studentAnswer = new StudentAnswer();
            studentAnswer.setQuestion(question);
            studentAnswer.setQuizAttempt(quizAttempt);
            studentAnswer.setSelectedAnswer(answer.getSelectedAnswer());

            studentAnswers.add(studentAnswer);

            if (question.getRightAnswer() != null
                    && answer.getSelectedAnswer() != null
                    && question.getRightAnswer().equalsIgnoreCase(answer.getSelectedAnswer())) {
                marks++;
            }
        }

        studentAnswerRepository.saveAll(studentAnswers);

        quizAttempt.setScore(marks);
        quizAttempt.setSubmittedAt(LocalDateTime.now());
        quizAttempt.setStatus(AttemptStatus.SUBMITTED);

        quizAttemptRepository.save(quizAttempt);
    }

    @Override
    public Integer calculateQuizScore(AnswerSubmissionRequestDto requestDto) {

        int marks = 0;

        for (StudentAnswerRequestDto answer : requestDto.getSubmittedAnswers()) {

            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new RuntimeException(
                            "Question not found: " + answer.getQuestionId()));

            if (question.getRightAnswer() != null
                    && question.getRightAnswer().equalsIgnoreCase(answer.getSelectedAnswer())) {
                marks++;
            }
        }

        return marks;
    }
}
