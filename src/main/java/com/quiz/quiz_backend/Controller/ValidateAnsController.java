package com.quiz.quiz_backend.Controller;

import com.quiz.quiz_backend.Entity.QuizQueEntity;
import com.quiz.quiz_backend.Entity.UserEntity;
import com.quiz.quiz_backend.Repository.UserRepo;
import com.quiz.quiz_backend.Service.ValidateAnsService;
import com.quiz.quiz_backend.dto.ValidateAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/validateAns")
public class ValidateAnsController
{
    @Autowired
    private ValidateAnsService validateAnsService;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<ValidateAnswers> validateAnswers,@RequestParam int quizId)
    {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> byUsername = userRepo.findByUsername(name);
        int userId = byUsername.get().getId();
        return validateAnsService.validateAnswer(validateAnswers,quizId,userId);
    }


    @GetMapping("/getWrongAns")
    public ResponseEntity<List<QuizQueEntity>> getWrongAns()
    {
        return validateAnsService.wrongAns();
    }
}
