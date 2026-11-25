package io.quizora.backend.dto;

import lombok.Data;

@Data
public class StudentAnswerRequestDto {

    private Integer questionId;
    private String selectedAnswer;
}
