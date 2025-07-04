package com.quiz.quiz_backend.Repository;

import com.quiz.quiz_backend.Entity.FileUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileUploadRepo extends JpaRepository<FileUploadEntity,Integer>
{
    Optional<FileUploadEntity> findByName(String name);
}
