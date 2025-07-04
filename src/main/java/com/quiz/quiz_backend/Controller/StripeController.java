package com.quiz.quiz_backend.Controller;

import com.quiz.quiz_backend.Entity.UserEntity;
import com.quiz.quiz_backend.Repository.UserRepo;
import com.quiz.quiz_backend.Service.StripeService;
import com.quiz.quiz_backend.dto.ProductRequest;
import com.quiz.quiz_backend.Entity.StripeResponse;
import com.quiz.quiz_backend.dto.SavePurchaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class StripeController
{
    @Autowired
    StripeService stripeService;

    @Autowired
    UserRepo userRepo;

    @PostMapping("/create-order")
    public StripeResponse createOrder(@RequestBody ProductRequest productRequest)
    {
        return stripeService.createOrder(productRequest);
    }

    @PostMapping("/checkSessionId")
    public ResponseEntity<String> checkSessionId(@RequestParam String sessionId, @RequestBody SavePurchaseDTO savePurchaseDTO)
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        UserEntity userEntity = userRepo.findByUsername(username).orElseThrow(()->new RuntimeException("No user found"));
        int id = userEntity.getId();
        System.out.println(id);
        return stripeService.checkSessionId(sessionId,savePurchaseDTO,id);
    }
}
