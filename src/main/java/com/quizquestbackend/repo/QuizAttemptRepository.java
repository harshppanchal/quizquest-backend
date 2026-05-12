package com.quizquestbackend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quizquestbackend.entity.QuizAttempt;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> { 

}