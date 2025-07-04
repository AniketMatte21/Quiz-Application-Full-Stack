package com.quiz.quiz_backend.Service;

import com.quiz.quiz_backend.Entity.UserEntity;
import com.quiz.quiz_backend.Entity.UserVerification;
import com.quiz.quiz_backend.Repository.UserRepo;
import com.quiz.quiz_backend.Repository.VerificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService
{

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    VerificationRepo verificationRepo;

    @Autowired
    EmailService emailService;

    private static final Logger logger=LoggerFactory.getLogger(UserService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found: "+username));
        return User.withUsername(user.getUsername()).password(user.getPassword()).build();

    }


    public ResponseEntity<String> detailValidation(UserEntity user)
    {
        String regex = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";

        if (!user.getEmail().matches(regex)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Email Format");
        }

        Optional<UserEntity> userByEmail = userRepo.findUserByEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
        }


        String name = user.getUsername();
        Optional<UserEntity> userByName = userRepo.findByUsername(name);
        if(userByName.isPresent())
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
        }

        String password = user.getPassword();
        int length=8;
        if(password.length()<length) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must contains minimum 8 characters");

        return ResponseEntity.ok().body("OTP Sent to your mail");

    }

    public ResponseEntity<String> addUser(UserEntity user, int Otp)
    {


        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<UserVerification> record = verificationRepo.findByEmailAndOtp(user.getEmail(),Otp);
        if(record.isPresent()){
            if(record.get().getExpiredTime().isBefore(LocalDateTime.now()))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP has expired");
            }
            userRepo.save(user);
            verificationRepo.delete(record.get());
            return ResponseEntity.ok("You have successfully created your account");
        }


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
    }
}
