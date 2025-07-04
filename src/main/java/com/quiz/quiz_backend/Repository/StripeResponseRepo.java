package com.quiz.quiz_backend.Repository;

import com.quiz.quiz_backend.Entity.StripeResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StripeResponseRepo extends JpaRepository<StripeResponse,Integer>
{
    Optional<StripeResponse> findBySessionId(String sessionId);

    @Transactional
    void deleteBySessionId(String sessionId);
}
