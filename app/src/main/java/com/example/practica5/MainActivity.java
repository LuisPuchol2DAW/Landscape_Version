package com.example.practica5;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private Questions questions;
    private Question currentQuestion;
    private static final String TAG = "MainActivity";
    private GestureDetector gestureDetector;

    private float startX, startY;
    private float endX, endY;
    private static TextView questionTextView;
    private static Button buttonTrue, buttonFalse, buttonNext, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Activity has been created");
        super.onCreate(savedInstanceState);
        startStuff();
        startLayout();
        startGestureAndQuestions();
        setButtonsListener();
    }

    public void setButtonsListener() {
        buttonTrue.setOnClickListener(v -> {
            Log.d(TAG, "Button True clicked!");
            currentQuestion = questions.getCurrentQuestion();
            if (currentQuestion.isCorrectAnswer()) {
                Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Incorrect!", Toast.LENGTH_SHORT).show();
            }
            currentQuestion.setAnswered(true);
            questions.moveToNextQuestion(questionTextView);
        });

        buttonFalse.setOnClickListener(v -> {
            Log.d(TAG, "Button False clicked!");
            currentQuestion = questions.getCurrentQuestion();
            if (!currentQuestion.isCorrectAnswer()) {
                Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Incorrect!", Toast.LENGTH_SHORT).show();
            }
            currentQuestion.setAnswered(true);
            questions.moveToNextQuestion(questionTextView);
        });

        buttonBack.setOnClickListener(v -> {
            Log.d(TAG, "Button Back clicked!");
            questions.moveToPreviousQuestion(questionTextView);
        });

        buttonNext.setOnClickListener(v -> {
            Log.d("MainActivity", "Button Next clicked!");
            questions.moveToNextQuestion(questionTextView);
        });
    }

    public void updateButtonIfAnswered(Question currentQuestion) {
        updateButtonState(currentQuestion.isAnswered());
    }

    private void updateButtonState(Boolean answered) {
        if (answered) {
            buttonTrue.setEnabled(false);
            buttonFalse.setEnabled(false);
        } else {
            buttonTrue.setEnabled(true);
            buttonFalse.setEnabled(true);
        }
    }


    public void startLayout() {
        buttonTrue = findViewById(R.id.buttonTrue);
        buttonFalse = findViewById(R.id.buttonFalse);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);
        questionTextView = findViewById(R.id.questionText);
    }

    public void startStuff() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void startGestureAndQuestions() {
        gestureDetector = new GestureDetector(this, this);
        questions = new Questions(questionTextView, this, 0);
    }

    private void updateQuestionUI() {
        currentQuestion = questions.getCurrentQuestion();
        currentQuestion.setQuestionText(questions.getCurrentQuestion().getQuestionText());
        updateButtonIfAnswered(currentQuestion);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("counter", questions.getCounter());
        outState.putSerializable("answeredStates", saveAnsweredStates());
    }

    private ArrayList<Boolean> saveAnsweredStates() {
        return questions.getQuestionList().stream()
                .map(Question::isAnswered)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
        protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int savedCounter = savedInstanceState.getInt("counter");
        restoreCurrentQuestion(savedCounter);
        restoreAnsweredStates(savedInstanceState);
        updateQuestionUI();
    }

    private void restoreCurrentQuestion(int savedCounter) {
        questions = new Questions(questionTextView, this, savedCounter);
    }

    private void restoreAnsweredStates(Bundle savedInstanceState) {
        ArrayList<Boolean> answeredStates = (ArrayList<Boolean>) savedInstanceState.getSerializable("answeredStates");
            for (int i = 0; i < answeredStates.size(); i++) {
                questions.getQuestionList().get(i).setAnswered(answeredStates.get(i));
            }
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //parte de abajo de la pantalla mayor valor Y
        //parte derecha de la pantalla mayor valor X
        Log.d(TAG, "onFling: Fling gesture detected with velocityX = " + velocityX + " and velocityY = " + velocityY);

        float movementX = startX - endX;
        float movementY = startY - endY;

        if (movementY > movementX && movementY > -movementX) {
            Log.d(TAG, "onFling: Fling gesture detected up");
        } else if (movementX > movementY && movementX > -movementY) {
            Log.d(TAG, "onFling: Fling gesture detected left");
        } else if (movementY > movementX && movementY < -movementX) {
            Log.d(TAG, "onFling: Fling gesture detected right");
        } else if (movementX > movementY && movementX < -movementY) {
            Log.d(TAG, "onFling: Fling gesture detected down");
        }

        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        startX = e.getX();
        startY = e.getY();
        Log.d(TAG, "onDown: User touched the screen");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(TAG, "onShowPress: User is pressing on the screen");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "onSingleTapUp: Single tap detected");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        endX = e2.getX();
        endY = e2.getY();
        Log.d(TAG, "onScroll: Scroll gesture detected with distanceX = " + distanceX + " and distanceY = " + distanceY);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(TAG, "onLongPress: Long press detected");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Activity is starting");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Activity has resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Activity is pausing");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Activity has stopped");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: Activity is restarting");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Activity is being destroyed");
    }
}