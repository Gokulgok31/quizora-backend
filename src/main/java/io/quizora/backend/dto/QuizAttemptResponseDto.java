package io.quizora.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QuizAttemptResponseDto {

    private Integer attemptId;
    private String rollNumber;
    private String studentName;
    private Integer quizId;
    private String status;
    private LocalDateTime startedAt;
}
