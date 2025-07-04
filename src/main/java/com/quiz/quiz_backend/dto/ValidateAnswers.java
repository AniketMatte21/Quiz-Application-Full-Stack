package com.quiz.quiz_backend.dto;

//for validating the answers
public class ValidateAnswers
{
    //id = questionId
    private int questionId;
    private String answer;

    public ValidateAnswers() {
    }

    public ValidateAnswers(int questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
