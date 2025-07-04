package com.quiz.quiz_backend.Service;

import com.quiz.quiz_backend.Entity.QuizOptEntity;
import com.quiz.quiz_backend.Entity.QuizQueEntity;
import com.quiz.quiz_backend.Entity.QuizType;
import com.quiz.quiz_backend.Repository.QuizRepo;
import com.quiz.quiz_backend.Repository.QuizTypeRepo;
import org.eclipse.angus.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizService
{
    @Autowired
    private QuizRepo quizRepo;
    @Autowired
    private QuizTypeRepo quizTypeRepo;

    public ResponseEntity<String> addQue(QuizQueEntity quizQueEntity)
    {
        if(quizQueEntity.getOptions()!=null)
        {
            for(QuizOptEntity quizOptEntity:quizQueEntity.getOptions())
            {
                quizOptEntity.setQuestions(quizQueEntity);
            }
            quizRepo.save(quizQueEntity);
            return ResponseEntity.ok("Question added..");

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Question adding failed");

    }

    public List<QuizQueEntity> getQue(String questionType)
    {
        String queType;
        if(questionType.equals("c++")){
            queType="cpp";
        }
        else {
            queType=questionType;
        }

        List<QuizQueEntity> byType = quizRepo.findByQuestionType(queType);
        Collections.shuffle(byType);
        return byType;

    }

    public ResponseEntity<String> addQuiz(QuizType quizType)
    {
        quizTypeRepo.save(quizType);
        return ResponseEntity.ok("Quiz Added to db");
    }

    public List<QuizType> getQuizes()
    {
        return quizTypeRepo.findAll();
    }
}
