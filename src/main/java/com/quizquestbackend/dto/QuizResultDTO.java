package com.quizquestbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizResultDTO {
    private int score;
    private int totalQuestions;
}