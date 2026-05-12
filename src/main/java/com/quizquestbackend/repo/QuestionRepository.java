package com.quizquestbackend.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.quizquestbackend.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(
        value = """
            SELECT * FROM questions
            WHERE difficulty = :difficulty
            AND category = :category
            ORDER BY RANDOM()
            LIMIT :limit
        """,
        nativeQuery = true
    )
    List<Question> findRandom(@Param("difficulty") String difficulty,@Param("category") String category,@Param("limit") int limit);
    @Query(value = """
    	    SELECT * FROM questions
    	    WHERE category = :category
    	    ORDER BY RANDOM()
    	    LIMIT :limit
    	""", nativeQuery = true)
    List<Question> findRandomByCategory(@Param("category") String category,@Param("limit") int limit);
    @Query(value = """
    	    SELECT * FROM questions
    	    WHERE difficulty = :difficulty
    	    ORDER BY RANDOM()
    	    LIMIT :limit
    	""", nativeQuery = true)
    List<Question> findRandomByDifficulty(@Param("difficulty") String difficulty,@Param("limit") int limit);
    @Query(value = """
    	    SELECT * FROM questions
    	    ORDER BY RANDOM()
    	    LIMIT :limit
    	""", nativeQuery = true)
    List<Question> findRandomAll(@Param("limit") int limit);
    Page<Question> findByCategoryAndDifficulty(String category,String difficulty,Pageable pageable);
    Page<Question> findByCategory(String category, Pageable pageable);
    Page<Question> findByDifficulty(String difficulty, Pageable pageable);
    
}