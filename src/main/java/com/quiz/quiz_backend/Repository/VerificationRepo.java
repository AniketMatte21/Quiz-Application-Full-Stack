package com.quiz.quiz_backend.Repository;

import com.quiz.quiz_backend.Entity.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepo extends JpaRepository<UserVerification,Integer>
{
    Optional<UserVerification> findByEmailAndOtp(String email, int Otp);
}
