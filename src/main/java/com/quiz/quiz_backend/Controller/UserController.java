package com.quiz.quiz_backend.Controller;

import com.quiz.quiz_backend.Entity.UserEntity;
import com.quiz.quiz_backend.Repository.UserRepo;
import com.quiz.quiz_backend.Service.EmailService;
import com.quiz.quiz_backend.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@CrossOrigin("http://192.168.137.177:5500")
@RestController
@RequestMapping("/api")
public class UserController
{
    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;


    @PostMapping(value = "/validate" , consumes = {"application/json","application/json;charset=UTF-8"})
    public ResponseEntity<String> detailValidation(@RequestBody UserEntity user)
    {
         ResponseEntity<String> res=userService.detailValidation(user);
         if(res.getStatusCode()== HttpStatusCode.valueOf(200))
         {
            sendOTP(user.getEmail());
         }
         return res;
    }

    public void sendOTP(String mail)
    {
        Random random=new Random();
        int otp=random.nextInt(900000)+100000;
        emailService.sendOTPmail(mail,otp);
    }


    @PostMapping("/doSignup")
    public ResponseEntity<String> doSignUp(@RequestBody UserEntity user, @RequestParam int Otp)
    {
        return userService.addUser(user,Otp);

    }


}
