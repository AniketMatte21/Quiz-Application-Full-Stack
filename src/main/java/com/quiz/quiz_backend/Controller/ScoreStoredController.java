package com.quiz.quiz_backend.Controller;

import com.quiz.quiz_backend.Service.ScoreStoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/performers")
public class ScoreStoredController
{
    @Autowired
    private ScoreStoringService scoreStoringService;

    @GetMapping("/topPerformers")
    public List<Object[]> getMaxScore()
    {
        return scoreStoringService.getTopPerformers();
    }

}
