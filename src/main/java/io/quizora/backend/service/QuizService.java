package io.quizora.backend.service;

import io.quizora.backend.dto.QuestionDto;
import io.quizora.backend.entity.Quiz;

import java.util.List;

public interface QuizService {

    Quiz createQuiz(Quiz quiz);

    List<Quiz> getAllQuiz();

    List<QuestionDto> generateQuiz(Integer quizId);

    String deleteQuiz(Integer id);
}
