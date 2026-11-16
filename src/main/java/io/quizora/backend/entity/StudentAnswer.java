package io.quizora.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String selectedAnswer;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @ManyToOne
    @JoinColumn(name = "quiz_attempt_id")
    private QuizAttempt quizAttempt;
}
