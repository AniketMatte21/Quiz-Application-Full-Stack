package com.quiz.quiz_backend.Repository;

import com.quiz.quiz_backend.Entity.QuizType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizTypeRepo extends JpaRepository<QuizType,Integer>
{
     List<QuizType> findByQuizTypeContaining(String name);
}
