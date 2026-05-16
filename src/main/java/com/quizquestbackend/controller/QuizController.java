package com.quizquestbackend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.quizquestbackend.dto.QuestionDTO;
import com.quizquestbackend.dto.QuizResultDTO;
import com.quizquestbackend.dto.QuizStartRequestDTO;
import com.quizquestbackend.dto.QuizSubmissionDTO;
import com.quizquestbackend.service.QuizService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/quiz")
public class QuizController {
	private final QuizService quizService;
	public QuizController(QuizService quizService) {
		this.quizService = quizService;
	}
	@PostMapping("/start")
	public List<QuestionDTO> startQuiz(@Valid @RequestBody QuizStartRequestDTO request) {
	    return quizService.startQuiz(request.getCategory(),request.getDifficulty(),request.getNumberOfQuestions());
	}
	@PostMapping("/submit")
	public QuizResultDTO submitQuiz(@RequestBody QuizSubmissionDTO submission) {
	    return quizService.submitQuiz(submission);
	}  
}