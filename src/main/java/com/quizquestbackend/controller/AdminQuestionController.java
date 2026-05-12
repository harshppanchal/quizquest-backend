package com.quizquestbackend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.quizquestbackend.dto.AdminQuestionDTO;
import com.quizquestbackend.dto.QuestionDTO;
import com.quizquestbackend.service.QuestionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/questions")
@PreAuthorize("hasRole('ADMIN')")
public class AdminQuestionController {

    private final QuestionService service;

    public AdminQuestionController(QuestionService service) {
        this.service = service;
    }
   
    @GetMapping
    public Page<QuestionDTO> getAllQuestions(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size,String category,String difficulty) {
        return service.getAllQuestions(page, size,category,difficulty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getQuestionById(id));
    }
    
    @PostMapping
    public ResponseEntity<QuestionDTO> add(@Valid @RequestBody AdminQuestionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addQuestion(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> update(@PathVariable Long id,@Valid @RequestBody AdminQuestionDTO dto) {
        return ResponseEntity.ok(service.updateQuestion(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}