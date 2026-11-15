package io.quizora.backend.controller;

import io.quizora.backend.entity.Exam;
import io.quizora.backend.entity.Question;
import io.quizora.backend.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @PostMapping("/create")
    public Question createQuestion(@RequestBody Question question) {
        return questionService.createQuestion(question);
    }

    @GetMapping("/list")
    public List<Question>  getQuizQuestions() {
        return questionService.getQuizQuestions();
    }

    @PutMapping("/update/{id}")
    public Question updateQuestion(@PathVariable Integer id, @RequestBody Question question) {
        return questionService.updateQuestion(id, question);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable Integer id) {
        return questionService.deleteQuestion(id);
    }

    @PatchMapping("/updateByPatch/{id}")
    public Question updateQuestionBySpecific(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        return questionService.updateQuestionBySpecific(id, updates);
    }

    @GetMapping("/{examCode}")
    public List<Question> getExamByExamCode(@PathVariable String examCode) {
        return questionService.getExamByExamCode(examCode);
    }
}
