package com.quizquestbackend.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log =LoggerFactory.getLogger(QuizServiceImpl.class);
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

    // ✅ START QUIZ
	@Override
	public List<QuestionDTO> startQuiz(
	        String category,
	        String difficulty,
	        int numberOfQuestions
	) {
	    log.info("Quiz started | category={} difficulty={}", category, difficulty);

	    if (numberOfQuestions <= 0) {
	        throw new IllegalArgumentException("Question count must be positive");
	    }

	    return questionRepository
	            .findRandom(difficulty, category, numberOfQuestions)
	            .stream()
	            .map(questionMapper::toDTO)
	            .toList();
	}

    // ✅ SUBMIT QUIZ
    @Override
    public QuizResultDTO submitQuiz(QuizSubmissionDTO submission) {

        int score = 0;
        int total = submission.getAnswers().size();

        for (var entry : submission.getAnswers().entrySet()) {
            Question question = questionRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Question not found"));

            if (question.getCorrectAnswer().equalsIgnoreCase(entry.getValue())) {
                score++;
            }
        }

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        QuizAttempt attempt = new QuizAttempt();
        attempt.setScore(score);
        attempt.setTotalQuestions(total);
        attempt.setDifficulty(submission.getDifficulty());
        attempt.setAttemptedAt(LocalDateTime.now());
        attempt.setUser(user);

        quizAttemptRepository.save(attempt);

        log.info("Quiz attempt saved | user={} score={}/{}", username, score, total);

        return new QuizResultDTO(score, total);
    }
}