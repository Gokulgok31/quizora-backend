package io.quizora.backend.repository;

import io.quizora.backend.entity.QuizAttempt;
import io.quizora.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Integer> {

    boolean existsByStudent_IdAndQuiz_Id(Integer studentId, Integer quizId);

    List<QuizAttempt> findByStudent_RollNumber(String rollNumber);
}
