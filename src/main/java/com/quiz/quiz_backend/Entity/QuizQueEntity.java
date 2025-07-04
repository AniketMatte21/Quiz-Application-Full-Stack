package com.quiz.quiz_backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Entity
@RequestMapping("/quiz")
public class QuizQueEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String question;
    private String questionType;

    public QuizQueEntity() {
    }

    public QuizQueEntity(String question, String questionType) {
        this.question = question;
        this.questionType = questionType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    @OneToMany(mappedBy = "questions", cascade = CascadeType.ALL)
    private List<QuizOptEntity> options;

    public List<QuizOptEntity> getOptions() {
        return options;
    }

    public void setOptions(List<QuizOptEntity> options) {
        this.options = options;
    }
}
