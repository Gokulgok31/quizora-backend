package io.quizora.backend.service.impl;

import io.quizora.backend.entity.Exam;
import io.quizora.backend.repository.ExamRepository;
import io.quizora.backend.service.ExamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    private ExamRepository examRepository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }
    @Override
    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }

    @Override
    public Exam updateExam(Integer id, Exam exam) {

        Exam exisitingExam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        exisitingExam.setExamCode(exam.getExamCode());
        return examRepository.save(exisitingExam);
    }

    @Override
    public String deleteExam(Integer id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        examRepository.deleteById(id);
        return "Exam with Id: " + id + " Deleted Successfully";
    }

    @Override
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

}
