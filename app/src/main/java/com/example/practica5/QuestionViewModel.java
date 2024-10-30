package com.example.practica5;

import androidx.lifecycle.ViewModel;

public class QuestionViewModel extends ViewModel {
    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void incrementCounter() {
        counter++;
    }

    public void decrementCounter() {
        counter--;
    }
}
