package com.quiz.quiz_backend.Controller;

import com.quiz.quiz_backend.Entity.UserEntity;
import com.quiz.quiz_backend.Entity.UserQuizPurchase;
import com.quiz.quiz_backend.Repository.UserRepo;
import com.quiz.quiz_backend.Service.QuizPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/quizPurchase")
public class QuizPurchasedController
{
    @Autowired
    UserRepo userRepo;

    @Autowired
    QuizPurchaseService quizPurchaseService;

    @GetMapping("/purchase")
    public ResponseEntity<?> checkPurchasedForUser()
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> byUsername = userRepo.findByUsername(username);
        if(byUsername.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user not found");
        }
        UserEntity user=byUsername.get();
        return quizPurchaseService.checkPurchasedForUser(user);
    }
}
