package io.quizora.backend.controller;


import io.quizora.backend.dto.QuestionDto;
import io.quizora.backend.dto.QuizAttemptRequestDto;
import io.quizora.backend.dto.QuizAttemptResponseDto;
import io.quizora.backend.entity.Question;
import io.quizora.backend.service.QuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz-attempt")
public class QuizAttemptController {

    @Autowired
    private QuizAttemptService quizAttemptService;


    @PostMapping("/start")
    public QuizAttemptResponseDto startQuiz(@RequestBody QuizAttemptRequestDto quizAttemptRequestDto) {
        return quizAttemptService.startQuiz(quizAttemptRequestDto);
    }
    @GetMapping("/{attemptId}/questions")
    public List<QuestionDto> generateQuestionsForQuizAttempt(@PathVariable Integer attemptId) {
        return quizAttemptService.generateQuestionsForQuizAttempt(attemptId);
    }
}
