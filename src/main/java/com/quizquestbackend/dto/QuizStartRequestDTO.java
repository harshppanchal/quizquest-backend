package com.quizquestbackend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizStartRequestDTO {

    @NotBlank
    private String category;

    @NotBlank
    private String difficulty;

    @Min(1)
    private int numberOfQuestions;
}