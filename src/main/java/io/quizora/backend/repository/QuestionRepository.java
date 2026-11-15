package io.quizora.backend.repository;

import io.quizora.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByExam_ExamCode(String examCode);

    long countByExam_ExamCode(String examCode);
}
