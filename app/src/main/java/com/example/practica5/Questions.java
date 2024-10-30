package com.example.practica5;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Questions {
    private List<Question> questionList;
    private Integer counter = 0;
    private Question question;
    private MainActivity mainActivity;


    public Questions(TextView questionText, MainActivity mainActivity, int initialCounter) {
        this.mainActivity = mainActivity;
        this.counter = initialCounter;
        questionList = new ArrayList<>();
        addQuestions();

        question = this.getQuestion(counter);
        questionText.setText(question.getQuestionText());
    }


    private void addQuestions() {
        questionList.add(new Question("¿Question1?", true, false));
        questionList.add(new Question("¿Question2?", false, false));
        questionList.add(new Question("¿Question3?", true, false));
    }

    public Question getCurrentQuestion() {
        return questionList.get(counter);
    }

    public Question getQuestion(int index) {
        if (index >= questionList.size()) {
            counter = 0;
        }
        return questionList.get(counter);
    }

    public void moveToNextQuestion(TextView questionText) {
        counter++;
        if (counter >= questionList.size()) {
            counter = 0;
        }
        question = this.getQuestion(counter);
        questionText.setText(question.getQuestionText());

        mainActivity.updateButtonIfAnswered(this.getCurrentQuestion());
    }

    public void moveToPreviousQuestion(TextView questionText) {
        counter --;
        if (counter < 0) {
            counter = questionList.size() -1;
        }
        question = this.getQuestion(counter);
        questionText.setText(question.getQuestionText());
        mainActivity.updateButtonIfAnswered(this.getCurrentQuestion());
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

}
