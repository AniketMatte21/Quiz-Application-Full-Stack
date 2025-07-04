package com.quiz.quiz_backend.Repository;

import com.quiz.quiz_backend.Entity.UserEntity;
import com.quiz.quiz_backend.Entity.UserQuizPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuizPurchaseRepo extends JpaRepository<UserQuizPurchase,Integer>
{
    List<UserQuizPurchase> findByUserEntity(UserEntity userEntity);
}
