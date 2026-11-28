package io.quizora.backend.service;

import io.quizora.backend.dto.*;
import io.quizora.backend.entity.Question;
import io.quizora.backend.entity.QuizAttempt;

import java.util.List;

public interface QuizAttemptService {

    QuizAttemptResponseDto startQuiz(QuizAttemptRequestDto quizAttemptRequestDto);

    List<QuestionDto> generateQuestionsForQuizAttempt(Integer attemptId);

    boolean validateQuizAttempt(QuizAttempt quizAttempt);

    boolean validateTimeLimit(QuizAttempt quizAttempt);

    boolean validateSubmittedQuestionId(QuizAttempt quizAttempt, StudentAnswerRequestDto studentAnswerRequestDto);

    Integer submitAnswers(AnswerSubmissionRequestDto answerSubmissionRequestDto);

    Integer calculateQuizScore(AnswerSubmissionRequestDto requestDto);

    boolean validateSubmittedOptions(QuizAttempt quizAttempt, AnswerSubmissionRequestDto requestDto);

}
