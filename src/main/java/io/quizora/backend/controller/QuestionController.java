package io.quizora.backend.controller;

import io.quizora.backend.entity.Question;
import io.quizora.backend.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @PostMapping("/createQuestion")
    public Question createQuestion(@RequestBody Question question) {
        return questionService.createQuestion(question);
    }
}
