package com.quiz.quiz_backend.Controller;

import com.quiz.quiz_backend.Entity.QuizQueEntity;
import com.quiz.quiz_backend.Entity.QuizType;
import com.quiz.quiz_backend.Service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController
{
    @Autowired
    private QuizService quizService;

    @PostMapping("/addQue")
    public ResponseEntity<String> addQuestions(@RequestBody QuizQueEntity quizQueEntity)
    {
        return quizService.addQue(quizQueEntity);
    }

    @GetMapping("/getQueByType")
    public List<QuizQueEntity> getQuestions(@RequestParam String questionType)
    {
        return quizService.getQue(questionType);
    }

    @PostMapping("/addQuiz")
    public ResponseEntity<String> addQuiz(@RequestBody QuizType quizType)
    {
        return quizService.addQuiz(quizType);
    }

    @GetMapping("/getQuizes")
    public List<QuizType> getQuizes()
    {
        return quizService.getQuizes();
    }





}
