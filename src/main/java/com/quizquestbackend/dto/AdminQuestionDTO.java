package com.quizquestbackend.dto;

import java.util.List;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminQuestionDTO {

    @NotBlank
    private String question;

    @Size(min = 4, max = 4)
    @NotEmpty
    private List<@NotBlank String> options;

    @NotBlank
    private int correctAnswer;

    @NotBlank
    private String difficulty;

    @NotBlank
    private String category;
}