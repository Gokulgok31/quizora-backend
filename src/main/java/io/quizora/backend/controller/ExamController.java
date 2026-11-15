package io.quizora.backend.controller;

import io.quizora.backend.entity.Exam;
import io.quizora.backend.service.ExamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam")
public class ExamController {

    private ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping("/create")
    public Exam createExam(@RequestBody Exam exam) {
        return examService.createExam(exam);
    }

    @PutMapping("/update/{id}")
    public Exam updateExam(@PathVariable Integer id, @RequestBody Exam exam) {
        return examService.updateExam(id, exam);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteExam(@PathVariable Integer id) {
        return examService.deleteExam(id);
    }

    @GetMapping("/getExams")
    public List<Exam> getAllExams() {
        return examService.getAllExams();
    }
}
