package com.quiz.quiz_backend.Service;

import com.quiz.quiz_backend.Entity.QuizType;
import com.quiz.quiz_backend.Entity.UserEntity;
import com.quiz.quiz_backend.Entity.UserQuizPurchase;
import com.quiz.quiz_backend.Repository.QuizTypeRepo;
import com.quiz.quiz_backend.Repository.UserQuizPurchaseRepo;
import com.quiz.quiz_backend.Repository.UserRepo;
import com.quiz.quiz_backend.dto.SavePurchaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizPurchaseService
{
    @Autowired
    private UserQuizPurchaseRepo userQuizPurchaseRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private QuizTypeRepo quizTypeRepo;

    public void savePurchased(SavePurchaseDTO savePurchaseDTO,int id)
    {
        Optional<UserEntity> userId = userRepo.findById(id);
        Optional<QuizType> quizCardId = quizTypeRepo.findById(savePurchaseDTO.getQuizCardId());

        if(userId.isPresent() && quizCardId.isPresent())
        {
            System.out.println("Present");
            UserQuizPurchase userQuizPurchase=new UserQuizPurchase();
            userQuizPurchase.setUserEntity(userId.get());
            userQuizPurchase.setQuizType(quizCardId.get());
            userQuizPurchase.setPaymentStatus(savePurchaseDTO.getPaymentStatus());
            userQuizPurchase.setPurchasedTime(LocalDateTime.now());

            userQuizPurchaseRepo.save(userQuizPurchase);

        }
        else {
            System.out.println("not present");
        }
    }

    public ResponseEntity<?> checkPurchasedForUser(UserEntity userEntity)
    {
        List<Integer> list=new ArrayList<>();
        List<UserQuizPurchase> userQuizPurchase = userQuizPurchaseRepo.findByUserEntity(userEntity);
        for(UserQuizPurchase quizCardObj:userQuizPurchase)
        {
            int quizCardId = quizCardObj.getQuizType().getId();
            list.add(quizCardId);
        }
        return ResponseEntity.ok(list);

    }
}
