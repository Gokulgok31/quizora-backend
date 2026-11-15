package io.quizora.backend.service;

import io.quizora.backend.entity.Exam;

import java.util.List;

public interface ExamService {

    Exam createExam(Exam exam);

    Exam updateExam(Integer id, Exam exam);

    String deleteExam(Integer id);

    List<Exam> getAllExams();
}
