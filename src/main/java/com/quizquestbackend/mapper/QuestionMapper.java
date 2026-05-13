package com.quizquestbackend.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import com.quizquestbackend.dto.AdminQuestionDTO;
import com.quizquestbackend.dto.QuestionDTO;
import com.quizquestbackend.entity.Question;

@Component
public class QuestionMapper {

    public QuestionDTO toDTO(Question q) {
        return new QuestionDTO(
        	    q.getId(),
        	    q.getQuestion(),
        	    List.of(q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD()),
        	    q.getCorrectAnswer(),
        	    q.getCategory(),
        	    q.getDifficulty()
        	);
    }

    public Question toEntity(AdminQuestionDTO dto) {
        Question q = new Question();
        q.setQuestion(dto.getQuestion());
        q.setOptionA(dto.getOptions().get(0));
        q.setOptionB(dto.getOptions().get(1));
        q.setOptionC(dto.getOptions().get(2));
        q.setOptionD(dto.getOptions().get(3));
        q.setCorrectAnswer(dto.getCorrectAnswer());
        q.setDifficulty(dto.getDifficulty());
        q.setCategory(dto.getCategory());
        return q;
    }
}