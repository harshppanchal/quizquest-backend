package com.quizquestbackend.service;

import com.quizquestbackend.dto.AdminQuestionDTO;
import com.quizquestbackend.dto.QuestionDTO;
import java.util.List;
import org.springframework.data.domain.Page;

public interface QuestionService {
    List<QuestionDTO> getQuestions(String category,String difficulty,int size);
    QuestionDTO addQuestion(AdminQuestionDTO dto);
    QuestionDTO updateQuestion(Long id, AdminQuestionDTO dto);
    void deleteQuestion(Long id);
	QuestionDTO getQuestionById(Long id);
	Page<QuestionDTO> getAllQuestions(int page, int size, String category, String difficulty);
}