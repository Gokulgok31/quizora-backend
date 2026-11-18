package io.quizora.backend.dto;

import io.quizora.backend.entity.Quiz;
import lombok.Data;

@Data
public class QuizAttemptRequestDto {

    private String rollNumber;
    private String studentName;
    private Integer quizId;
}
