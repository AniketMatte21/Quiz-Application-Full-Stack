package com.quiz.quiz_backend.Repository;

import com.quiz.quiz_backend.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity,Integer>
{
    Optional<UserEntity> findByUsername(String name);
    Optional<UserEntity> findUserByEmail(String email);
}
