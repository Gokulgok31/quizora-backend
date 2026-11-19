package io.quizora.backend.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class AnswerSubmissionRequestDto {

    private Integer attemptId;
    private List<AnswerRequestDto> submittedAnswers;
}
