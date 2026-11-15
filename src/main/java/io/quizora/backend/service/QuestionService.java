package io.quizora.backend.service;


import io.quizora.backend.entity.Question;
import io.quizora.backend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public interface QuestionService {

    Question createQuestion(Question question);

    List<Question> getQuizQuestions();

    Question updateQuestion(Integer id, Question updatedQuestion);

    String deleteQuestion(Integer id);

    Question updateQuestionBySpecific(Integer id, Map<String, Object> updates);

    List<Question> getExamByExamCode(String examCode);
}
