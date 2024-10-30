package com.example.practica5;

public class Question {
    private String questionText;
    private boolean correctAnswer;
    private boolean answered;

    public Question(String questionText, boolean correctAnswer, boolean answered) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.answered = false;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }

}
