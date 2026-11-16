package io.quizora.backend.controller;

import io.quizora.backend.dto.QuestionDto;
import io.quizora.backend.entity.Question;
import io.quizora.backend.entity.Quiz;
import io.quizora.backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/create")
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    @GetMapping("/getQuiz")
    public List<Quiz> getAllQuiz() {
        return quizService.getAllQuiz();
    }

    @GetMapping("/generate/{quizId}")
    public List<QuestionDto> generateQuiz(@PathVariable Integer quizId) {
        return quizService.generateQuiz(quizId);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteQuiz(@PathVariable Integer id) {
        return quizService.deleteQuiz(id);
    }

}
