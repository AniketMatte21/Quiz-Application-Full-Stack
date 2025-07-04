package com.quiz.quiz_backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavePurchaseDTO
{
    private int quizCardId;
    private String paymentStatus;
}
