package io.quizora.backend.dto;

import lombok.Data;

@Data
public class StudentAnswerRequestDto {

    Integer questionId;
    String selectedAnswer;
}
