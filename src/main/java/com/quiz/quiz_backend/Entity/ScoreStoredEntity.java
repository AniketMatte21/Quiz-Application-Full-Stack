package com.quiz.quiz_backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreStoredEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scoreId;
    private int score;

    @ManyToOne
    @JsonBackReference(value = "user_score")
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JsonBackReference(value = "quiz_score")
    @JoinColumn(name="quiz_id")
    private QuizType quizType;



}
