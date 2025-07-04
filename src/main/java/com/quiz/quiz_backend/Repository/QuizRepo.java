package com.quiz.quiz_backend.Repository;

import com.quiz.quiz_backend.Entity.QuizQueEntity;
import com.quiz.quiz_backend.Entity.QuizType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizRepo extends JpaRepository<QuizQueEntity,Integer>
{
    List<QuizQueEntity> findByQuestionType(String questionType);
}
