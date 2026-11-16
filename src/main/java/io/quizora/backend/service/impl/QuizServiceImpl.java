package io.quizora.backend.service.impl;

import io.quizora.backend.dto.QuestionDto;
import io.quizora.backend.entity.Exam;
import io.quizora.backend.entity.Question;
import io.quizora.backend.entity.Quiz;
import io.quizora.backend.repository.ExamRepository;
import io.quizora.backend.repository.QuestionRepository;
import io.quizora.backend.repository.QuizRepository;
import io.quizora.backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Quiz createQuiz(Quiz quiz) {
        String examCode = quiz.getExam().getExamCode();

        Exam exam = examRepository.findByExamCode(examCode)
                .orElseThrow(() -> new RuntimeException("ExamCode not found"));

        int availableQuestions =
                questionRepository.countByExam_ExamCode(examCode);
        quiz.setNumberOfQuestions(availableQuestions);
        int passingMarks = calculatePassMark(quiz, availableQuestions);
        quiz.setPassingMarks(passingMarks);
        quiz.setExam(exam);
        return quizRepository.save(quiz);
    }

    private static int calculatePassMark(Quiz quiz, long availableQuestions) {
        Integer requestedQuestions = quiz.getNumberOfQuestions();

        if (requestedQuestions == null || requestedQuestions <= 0) {
            throw new RuntimeException(
                    "Number of questions must be greater than zero");
        }

        if (requestedQuestions > availableQuestions) {
            throw new RuntimeException(
                    "Only " + availableQuestions +
                            " questions available for this exam");
        }
        return (int) Math.ceil(
                requestedQuestions * 0.40
        );
    }

    @Override
    public List<Quiz> getAllQuiz() {
        return quizRepository.findAll();
    }

    @Override
    public List<QuestionDto> generateQuiz(Integer quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz Not found with Id: " + quizId));

        String examCode = quiz.getExam().getExamCode();

        List<Question> questions = questionRepository.findByExam_ExamCode(examCode);
        List<QuestionDto> questionList = new ArrayList<>();
        for(Question question: questions) {
            QuestionDto questionDto = new QuestionDto();
            questionDto.setId(question.getId());
            questionDto.setQuestionTitle(question.getQuestionTitle());
            questionDto.setOption1(question.getOption1());
            questionDto.setOption2(question.getOption2());
            questionDto.setOption3(question.getOption3());
            questionDto.setOption4(question.getOption4());
            questionList.add(questionDto);

        }
        return questionList;
    }

    @Override
    public String deleteQuiz(Integer id) {
        if(!quizRepository.existsById(id)) {
            throw new RuntimeException("Id not found");
        }
        quizRepository.deleteById(id);
        return "Quiz with Id: "+ id +" deleted successfully";
    }
}