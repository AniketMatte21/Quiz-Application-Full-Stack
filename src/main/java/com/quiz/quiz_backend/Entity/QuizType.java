package com.quiz.quiz_backend.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
public class QuizType
{
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Nonnull
    private String quizType;

    @Setter
    @Getter
    private double price;

    @Setter
    @Getter
    private String quizDescription;

    @Setter
    @Getter
    private String imgUrl;

    public QuizType() {
    }

    public QuizType(@Nonnull String quizType, double price, String quizDescription, String imgUrl) {
        this.quizType = quizType;
        this.price = price;
        this.quizDescription = quizDescription;
        this.imgUrl = imgUrl;
    }

    @Nonnull
    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(@Nonnull String quizType) {
        this.quizType = quizType;
    }

    @Setter
    @Getter
    @OneToMany(mappedBy = "quizType", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "quiz_purchase_list")
    private List<UserQuizPurchase> userQuizPurchaseList;


    @Getter
    @Setter
    @OneToMany(mappedBy = "quizType",cascade = CascadeType.ALL)
    @JsonManagedReference(value = "quiz_score")
    private List<ScoreStoredEntity> scoreStoredEntity;



}
