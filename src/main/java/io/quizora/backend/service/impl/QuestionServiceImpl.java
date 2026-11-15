package io.quizora.backend.service.impl;

import io.quizora.backend.entity.Exam;
import io.quizora.backend.entity.Question;
import io.quizora.backend.repository.ExamRepository;
import io.quizora.backend.repository.QuestionRepository;
import io.quizora.backend.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ExamRepository examRepository;

    public Question createQuestion(Question question) {
        String examCode =  question.getExam().getExamCode();
        Exam exam = examRepository.findByExamCode(examCode)
                .orElseThrow(() ->
                        new RuntimeException("Exam not found: " + examCode));

        question.setExam(exam);
        return questionRepository.save(question);
    }

    public List<Question> getQuizQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Question updateQuestion(Integer id, Question updatedQuestion) {

        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question Not Found"));
        existingQuestion.setQuestionTitle(updatedQuestion.getQuestionTitle());
        existingQuestion.setOption1(updatedQuestion.getOption1());
        existingQuestion.setOption2(updatedQuestion.getOption2());
        existingQuestion.setOption3(updatedQuestion.getOption3());
        existingQuestion.setOption4(updatedQuestion.getOption4());
        existingQuestion.setRightAnswer(updatedQuestion.getRightAnswer());
        existingQuestion.setDifficultyLevel(updatedQuestion.getDifficultyLevel());
        String examCode = updatedQuestion.getExam().getExamCode();

        Exam exam = examRepository.findByExamCode(examCode)
                .orElseThrow(() ->
                        new RuntimeException("Exam not found"));

        existingQuestion.setExam(exam);

        return questionRepository.save(existingQuestion);
    }

    @Override
    public String deleteQuestion(Integer id) {
        Question question = questionRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Question not found"));
        questionRepository.deleteById(id);
        return "Question Deleted Successfully";
    }

    @Override
    public Question updateQuestionBySpecific(Integer id, Map<String, Object> updates) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        updates.forEach((key, value) -> {
            switch(key) {
                case "questionTitle" ->
                                    question.setQuestionTitle((String) value);

                case "option1" ->
                        question.setOption1((String) value);

                case "option2" ->
                        question.setOption2((String) value);

                case "option3" ->
                        question.setOption3((String) value);

                case "option4" ->
                        question.setOption4((String) value);

                case "rightAnswer" ->
                        question.setRightAnswer((String) value);

                case "difficultyLevel" ->
                        question.setDifficultyLevel((String) value);

                case "exam" -> {
                    Map<String, Object> examData =
                            (Map<String, Object>) value;

                    String examCode = (String) examData.get("examCode");

                    Exam exam = examRepository.findByExamCode(examCode)
                            .orElseThrow(() ->
                                    new RuntimeException("Exam Not Found"));

                    question.setExam(exam);
                }

                default -> throw new IllegalArgumentException(
                        "Invalid field: " + key);
            }
        });
        return questionRepository.save(question);
    }

    @Override
    public List<Question> getExamByExamCode(String examCode) {
        Exam exam = examRepository.findByExamCode(examCode)
                .orElseThrow(() -> new RuntimeException("Exam Code not found"));

        return questionRepository.findByExam_ExamCode(examCode);
    }

}
