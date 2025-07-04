package com.quiz.quiz_backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.yaml.snakeyaml.events.Event;

@Entity
@Table(name = "quiz_opt_entity")
public class QuizOptEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int optId;

    private String opt;

    private boolean isCorrect;

    public QuizOptEntity() {
    }

    public QuizOptEntity(String opt, boolean isCorrect) {
        this.opt = opt;
        this.isCorrect = isCorrect;
    }

    public int getOptId() {
        return optId;
    }

    public void setOptId(int optId) {
        this.optId = optId;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    @ManyToOne
    @JoinColumn(name = "questions_id")
    @JsonIgnore
    private QuizQueEntity questions;

    public QuizQueEntity getQuestions() {
        return questions;
    }

    public void setQuestions(QuizQueEntity questions) {
        this.questions = questions;
    }
}
