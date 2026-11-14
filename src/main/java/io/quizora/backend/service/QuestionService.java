package io.quizora.backend.service;


import io.quizora.backend.entity.Question;
import io.quizora.backend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface QuestionService {

    Question createQuestion(Question question);

    List<Question> getQuizQuestions();

}
