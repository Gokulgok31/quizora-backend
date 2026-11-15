package io.quizora.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String quizTitle;

    private Integer numberOfQuestions;

    private Integer durationInMinutes;

    private Integer passingMarks;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

}
