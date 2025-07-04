package com.quiz.quiz_backend.Service;

import com.quiz.quiz_backend.Entity.UserVerification;
import com.quiz.quiz_backend.Repository.UserRepo;
import com.quiz.quiz_backend.Repository.VerificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService
{
    @Autowired
    public JavaMailSender javaMailSender;
    @Autowired
    private JavaMailSenderImpl mailSender;


    @Autowired
    VerificationRepo verificationRepo;

    @Autowired
    UserRepo userRepo;

    public void sendOTPmail(String toMail, Integer OTP) {

        UserVerification uv = new UserVerification();
        uv.setEmail(toMail);
        uv.setOtp(OTP);
        uv.setExpiredTime(LocalDateTime.now().plusMinutes(5));
        verificationRepo.save(uv);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toMail);
        msg.setSubject("Your OTP");
        msg.setText("OTP: " + OTP);
        mailSender.send(msg);



    }

}
