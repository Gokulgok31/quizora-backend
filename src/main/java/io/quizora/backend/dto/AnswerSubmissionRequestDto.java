package io.quizora.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnswerSubmissionRequestDto {

    private Integer attemptId;
    private List<StudentAnswerRequestDto> submittedAnswers;
}
