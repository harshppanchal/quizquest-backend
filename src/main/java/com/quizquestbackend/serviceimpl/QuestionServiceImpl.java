package com.quizquestbackend.serviceimpl;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.quizquestbackend.mapper.QuestionMapper;
import com.quizquestbackend.dto.AdminQuestionDTO;
import com.quizquestbackend.dto.QuestionDTO;
import com.quizquestbackend.entity.Question;
import com.quizquestbackend.repo.QuestionRepository;
import com.quizquestbackend.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository repo;
    private final QuestionMapper mapper;

    public QuestionServiceImpl(QuestionRepository repo, QuestionMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public List<QuestionDTO> getQuestions(String category, String difficulty, int size) {
        List<Question> result;
        if (category != null && difficulty != null) {
            result = repo.findRandom(difficulty, category, size);
        } else if (category != null) {
            result = repo.findRandomByCategory(category, size);
        } else if (difficulty != null) {
            result = repo.findRandomByDifficulty(difficulty, size);
        } else {
            result = repo.findRandomAll(size);
        }
        return result.stream().map(mapper::toDTO).toList();
    }

    @Override
    public QuestionDTO addQuestion(AdminQuestionDTO dto) {
        // 1️⃣ Enforce exactly 4 options (CRITICAL)
        if (dto.getOptions() == null || dto.getOptions().size() != 4) {
            throw new IllegalArgumentException("Exactly 4 options are required");
        }
        // 2️⃣ Correct answer must be one of the options
        if (!dto.getOptions().contains(dto.getCorrectAnswer())) {
            throw new IllegalArgumentException("Correct answer must be one of the options");
        }
        // 3️⃣ Save only after validation
        Question saved = repo.save(mapper.toEntity(dto));
        return mapper.toDTO(saved);
    }

    @Override
    public QuestionDTO updateQuestion(Long id, AdminQuestionDTO dto) {
        if (dto.getOptions() == null || dto.getOptions().size() != 4) {
            throw new IllegalArgumentException("Exactly 4 options are required");
        }
        int answer = dto.getCorrectAnswer();
        if (answer < 0 || answer > 3) {
            throw new IllegalArgumentException("Correct answer must be between 0 and 3");
        }
        Question existing = repo.findById(id) .orElseThrow(() -> new IllegalArgumentException("Question not found"));
        existing.setQuestion(dto.getQuestion());
        existing.setOptionA(dto.getOptions().get(0));
        existing.setOptionB(dto.getOptions().get(1));
        existing.setOptionC(dto.getOptions().get(2));
        existing.setOptionD(dto.getOptions().get(3));
        existing.setCorrectAnswer(dto.getCorrectAnswer());
        existing.setDifficulty(dto.getDifficulty());
        existing.setCategory(dto.getCategory());
        return mapper.toDTO(repo.save(existing));
    }

    @Override
    public void deleteQuestion(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Question not found");
        }
        repo.deleteById(id);
    }

    @Override
    public Page<QuestionDTO> getAllQuestions(int page, int size, String category, String difficulty) {

        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Question> result;

        if (category != null && difficulty != null) {
            result = repo.findByCategoryAndDifficulty(category, difficulty, pageable);
        } else if (category != null) {
            result = repo.findByCategory(category, pageable);
        } else if (difficulty != null) {
            result = repo.findByDifficulty(difficulty, pageable);
        } else {
            result = repo.findAll(pageable);
        }

        return result.map(mapper::toDTO);
    }

    @Override
    public QuestionDTO getQuestionById(Long id) {
        Question q = repo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Question not found"));
        return mapper.toDTO(q);
    }
}