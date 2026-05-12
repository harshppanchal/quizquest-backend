package com.quizquestbackend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionDTO {
    private Long id;
    private String question;
    private List<String> options;
    private String correctAnswer;
    private String category;
    private String difficulty;
}