package io.quizora.backend.service;

import io.quizora.backend.dto.QuestionDto;
import io.quizora.backend.dto.QuizAttemptRequestDto;
import io.quizora.backend.dto.QuizAttemptResponseDto;
import io.quizora.backend.entity.Question;
import io.quizora.backend.entity.QuizAttempt;

import java.util.List;

public interface QuizAttemptService {

    QuizAttemptResponseDto startQuiz(QuizAttemptRequestDto quizAttemptRequestDto);

    List<QuestionDto> generateQuestionsForQuizAttempt(Integer attemptId);
}
