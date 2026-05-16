package com.quizquestbackend.serviceimpl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.quizquestbackend.entity.User;
import com.quizquestbackend.mapper.QuestionMapper;
import com.quizquestbackend.repo.UserRepository;
import com.quizquestbackend.dto.QuestionDTO;
import com.quizquestbackend.dto.QuizResultDTO;
import com.quizquestbackend.dto.QuizSubmissionDTO;
import com.quizquestbackend.entity.Question;
import com.quizquestbackend.entity.QuizAttempt;
import com.quizquestbackend.repo.QuestionRepository;
import com.quizquestbackend.repo.QuizAttemptRepository;
import com.quizquestbackend.service.QuizService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class QuizServiceImpl implements QuizService {
	private final QuestionRepository questionRepository;
	private final QuizAttemptRepository quizAttemptRepository;
	private final UserRepository userRepository;
	private final QuestionMapper questionMapper;
	public QuizServiceImpl(QuestionRepository questionRepository,QuizAttemptRepository quizAttemptRepository,UserRepository userRepository,QuestionMapper questionMapper) {
	    this.questionRepository = questionRepository;
	    this.quizAttemptRepository = quizAttemptRepository;
	    this.userRepository = userRepository;
	    this.questionMapper = questionMapper;
	}
	@Override
	public List<QuestionDTO> startQuiz(String category,
	        String difficulty,int numberOfQuestions
	) {
	    if (numberOfQuestions <= 0) {
	        throw new IllegalArgumentException("Question count must be positive");
	    }
	    return questionRepository
	            .findRandom(difficulty, category, numberOfQuestions)
	            .stream()
	            .map(questionMapper::toDTO)
	            .toList();
	}

    @Override
    public QuizResultDTO submitQuiz(QuizSubmissionDTO submission) {
        int score = 0;
        int total = submission.getAnswers().size();
        for (var entry : submission.getAnswers().entrySet()) {
            Question question = questionRepository.findById(entry.getKey())
.orElseThrow(() -> new IllegalArgumentException("Question not found"));
            String submittedAnswer = entry.getValue(); 
            String correctOptionText = null;
            switch (question.getCorrectAnswer()) {
                case "A" -> correctOptionText = question.getOptionA();
                case "B" -> correctOptionText = question.getOptionB();
                case "C" -> correctOptionText = question.getOptionC();
                case "D" -> correctOptionText = question.getOptionD();
                default -> throw new IllegalStateException("Invalid correctAnswer value: " + question.getCorrectAnswer());
            }
            if (correctOptionText.equalsIgnoreCase(submittedAnswer)) {
                score++;
            }
        }
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        QuizAttempt attempt = new QuizAttempt();
        attempt.setScore(score);
        attempt.setCorrectAnswers(score);    
        attempt.setTotalQuestions(total);
        attempt.setCategory(submission.getCategory());
        attempt.setDifficulty(submission.getDifficulty());
        attempt.setUser(user);
        quizAttemptRepository.save(attempt);
        return new QuizResultDTO(score, total);
    }
}