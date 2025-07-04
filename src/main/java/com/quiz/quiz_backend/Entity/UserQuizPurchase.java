package com.quiz.quiz_backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

@Entity
@Table(name="User_Quiz_Purchase")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQuizPurchase
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String paymentStatus;

    private LocalDateTime purchasedTime;

    public UserQuizPurchase(String paymentStatus, LocalDateTime purchasedTime) {
        this.paymentStatus = paymentStatus;
        this.purchasedTime = purchasedTime;
    }

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference(value = "user_entity")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name="quiz_card_id")
    @JsonBackReference(value = "quiz_purchase_list")
    private QuizType quizType;


}
