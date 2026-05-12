package com.quizquestbackend.service;

import java.util.List;
import com.quizquestbackend.dto.QuestionDTO;
import com.quizquestbackend.dto.QuizResultDTO;
import com.quizquestbackend.dto.QuizSubmissionDTO;

public interface QuizService {

    List<QuestionDTO> startQuiz(String category,String difficulty,int numberOfQuestions);
    QuizResultDTO submitQuiz(QuizSubmissionDTO submission);
}