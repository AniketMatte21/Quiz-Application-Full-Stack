package com.quiz.quiz_backend.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StripeResponse
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String status;
    private String message;
    private String sessionId;
    @Column(length = 1000)
    private String sessionUrl;
}
