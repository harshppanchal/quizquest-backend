package com.quizquestbackend.dto;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizSubmissionDTO {
	private String category;
    private String difficulty;
    private Map<Long, String> answers;
}