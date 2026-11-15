package io.quizora.backend.service;

import io.quizora.backend.dto.QuestionDto;
import io.quizora.backend.entity.Question;
import io.quizora.backend.entity.Quiz;

import java.util.List;

public interface QuizService {

    Quiz createQuiz(Quiz quiz);

    List<Quiz> getAllQuiz();

    List<QuestionDto> generateQuiz(Integer quizId);
}
