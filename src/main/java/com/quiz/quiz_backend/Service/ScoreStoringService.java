package com.quiz.quiz_backend.Service;

import com.quiz.quiz_backend.Repository.ScoreStoringRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreStoringService
{
    @Autowired
    private ScoreStoringRepo scoreStoringRepo;

    public List<Object[]> getTopPerformers()
    {
        List<Object[]> topPerformers = scoreStoringRepo.getTopPerformers();
        return topPerformers;
    }
}
