package io.quizora.backend.service;

import io.quizora.backend.dto.QuizAttemptRequestDto;
import io.quizora.backend.dto.QuizAttemptResponseDto;
import io.quizora.backend.entity.QuizAttempt;

public interface QuizAttemptService {

    QuizAttemptResponseDto startQuiz(QuizAttemptRequestDto quizAttemptRequestDto);
}
