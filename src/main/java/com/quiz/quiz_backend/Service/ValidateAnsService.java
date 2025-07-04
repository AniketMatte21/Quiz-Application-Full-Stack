package com.quiz.quiz_backend.Service;

import com.quiz.quiz_backend.Entity.*;
import com.quiz.quiz_backend.Repository.QuizRepo;
import com.quiz.quiz_backend.Repository.QuizTypeRepo;
import com.quiz.quiz_backend.Repository.ScoreStoringRepo;
import com.quiz.quiz_backend.Repository.UserRepo;
import com.quiz.quiz_backend.dto.ValidateAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ValidateAnsService
{
    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private ScoreStoringRepo scoreStoringRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private QuizTypeRepo quizTypeRepo;

    List<Integer> wrongQueId=new ArrayList<>();

    public ResponseEntity<Integer> validateAnswer(List<ValidateAnswers> validateAnswers,int quizId,int userId)
    {
        int score=0;

        for(ValidateAnswers item:validateAnswers)
        {
            int id=item.getQuestionId();
            Optional<QuizQueEntity> byId = quizRepo.findById(id);
            if(byId.isEmpty()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(0);//question not found
            }

            String answer = item.getAnswer();
            List<QuizOptEntity> options = byId.get().getOptions();
            //initially
            boolean isCorrect=false;

            for(QuizOptEntity opt:options)
            {

                if(opt.getOpt().equals(answer) && opt.getIsCorrect())
                {
                    score=score+1;
                    //if opt get right then set true
                    isCorrect=true;
                    break;

                }

            }
            //if not correct add to the list
            if(!isCorrect)
            {
                System.out.println(id);
                wrongQueId.add(id);
            }

        }
        Optional<UserEntity> byId = userRepo.findById(userId);
        UserEntity userEntity=null;
        if(byId.isPresent())
        {
            userEntity=byId.get();
        }

        Optional<QuizType> byId1 = quizTypeRepo.findById(quizId);
        QuizType quizType=null;
        if(byId1.isPresent())
        {
            quizType=byId1.get();
        }


        scoreStoringRepo.save(
                ScoreStoredEntity.builder()
                        .score(score)
                        .userEntity(userEntity)
                        .quizType(quizType)
                        .build()
        );
        return ResponseEntity.ok(score);
    }

    public ResponseEntity<List<QuizQueEntity>> wrongAns()
    {
        List<QuizQueEntity> list=new ArrayList<>();
        for(Integer id:wrongQueId)
        {
            Optional<QuizQueEntity> byId = quizRepo.findById(id);
            if(byId.isPresent())
            {
                QuizQueEntity quizQueEntity = byId.get();
                list.add(quizQueEntity);
            }
        }

        wrongQueId.clear();
        return ResponseEntity.ok(list);
    }
}
