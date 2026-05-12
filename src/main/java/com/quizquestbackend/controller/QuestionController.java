package com.quizquestbackend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.quizquestbackend.dto.QuestionDTO;
import com.quizquestbackend.service.QuestionService;

@RestController
@RequestMapping("/api")
public class QuestionController {

    private final QuestionService service;

    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @GetMapping("/questions")
    public List<QuestionDTO> getQuestions(@RequestParam(required = false) String category,@RequestParam(required = false) String difficulty,@RequestParam(defaultValue = "5") int size) {
        return service.getQuestions(category,difficulty,size);
    }
}