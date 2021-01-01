package com.mkdev.trivai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mkdev.trivai.controller.AppController;
import com.mkdev.trivai.data.AnswerListAsyncResponse;
import com.mkdev.trivai.data.QuestionBank;
import com.mkdev.trivai.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button getDataBtn;
    private TextView editTextNumber;
    private TextView questionViewText;
    private TextView textViewQuestionCounter1;
    private Button buttonTrue;
    private Button buttonFalse;
    private ImageButton imageButtonPrev;
    private ImageButton imageButtonNext;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNumber = findViewById(R.id.editTextNumber);
        getDataBtn = findViewById(R.id.getDataBtn);
        imageButtonPrev = findViewById(R.id.imageButtonPrev);
        imageButtonNext = findViewById(R.id.imageButtonNext);
        buttonTrue = findViewById(R.id.buttonTrue);
        buttonFalse = findViewById(R.id.buttonFalse);
        questionViewText = findViewById(R.id.questionViewText);
        textViewQuestionCounter1 = findViewById(R.id.textViewQuestionCounter1);

        imageButtonPrev.setOnClickListener(this);
        imageButtonNext.setOnClickListener(this);
        buttonTrue.setOnClickListener(this);
        buttonFalse.setOnClickListener(this);
        getDataBtn.setOnClickListener(this);


//        getDataBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
//                    @Override
//                    public void processFinished(ArrayList<Question> questionArrayList) {
//                        Log.d("Questions", "onClick: " + questionArrayList);
//                    }
//                });
//            }
//        });

    }

    public void getQuestions() {
        currentQuestionIndex = 0;
        textViewQuestionCounter1.setText(0 + " out of " + 0);

        int numOfQues = 10;
        if (editTextNumber.getText() != null && editTextNumber.getText() != "") {
            Log.d("NumberOF", "getQuestions: " + editTextNumber.getText());
            numOfQues = Integer.parseInt(editTextNumber.getText().toString());
        }

        questionList = new QuestionBank().getQuestions(numOfQues, new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
//                Log.d("Questions", "onClick: " + questionArrayList);
                questionViewText.setText(questionArrayList.get(currentQuestionIndex).getQuestion());
                textViewQuestionCounter1.setText((currentQuestionIndex + 1) + " out of " + questionList.size());
            }
        });
    }

    public void moveToNextQuestion() {
        String question = questionList.get(currentQuestionIndex).getQuestion();
        questionViewText.setText(question);
        textViewQuestionCounter1.setText((currentQuestionIndex + 1) + " out of " + questionList.size());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonPrev:
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                }
                moveToNextQuestion();
                break;
            case R.id.imageButtonNext:
                Log.d("onClick", "onClick: " + currentQuestionIndex);
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                Log.d("onClick", "onClick: " + currentQuestionIndex);
                moveToNextQuestion();
                break;
            case R.id.buttonTrue:
                checkAnswer(true);
                moveToNextQuestion();
                break;
            case R.id.buttonFalse:
                checkAnswer(false);
                moveToNextQuestion();
                break;
            case R.id.getDataBtn:
                getQuestions();
                break;
        }
    }

    private void checkAnswer(boolean userChoice) {
        boolean isAnswerCorrect = questionList.get(currentQuestionIndex).isAnswer();

        int toastMessageId = 0;
        if (userChoice == isAnswerCorrect) {
            toastMessageId = R.string.correct_answer;
            fadeView();
        } else {
            toastMessageId = R.string.wrong_answer;
            shakeAnimation();
        }

        Toast.makeText(MainActivity.this, toastMessageId, Toast.LENGTH_SHORT).show();
    }

    private void fadeView() {
        CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}