package io.quizora.backend.repository;

import io.quizora.backend.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {

    Optional<Exam> findByExamCode(String examCode);
}
