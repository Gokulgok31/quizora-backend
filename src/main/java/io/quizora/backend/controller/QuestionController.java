package io.quizora.backend.controller;

import io.quizora.backend.entity.Question;
import io.quizora.backend.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question")
public class QuestionController {

    private QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    public ResponseEntity<Question> createQuestion(Question question) {
        questionService.createQuestion(question);
        return ResponseEntity.ok("Question Created");
    }
}
