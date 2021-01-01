package com.mkdev.trivai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mkdev.trivai.controller.AppController;
import com.mkdev.trivai.data.AnswerListAsyncResponse;
import com.mkdev.trivai.data.QuestionBank;
import com.mkdev.trivai.model.Question;
import com.mkdev.trivai.model.Score;
import com.mkdev.trivai.util.AppPrefs;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button getDataBtn;
    private TextView editTextNumber;
    private TextView textViewScore;
    private TextView questionViewText;
    private TextView textViewHighestScore;
    private TextView textViewQuestionCounter1;
    private Button buttonTrue;
    private Button buttonFalse;
    private ImageButton imageButtonPrev;
    private ImageButton imageButtonNext;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;
    private int scoreCounter = 0;
    private Score score;
    private AppPrefs appPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = new Score();
        appPrefs = new AppPrefs(MainActivity.this);

        editTextNumber = findViewById(R.id.editTextNumber);
        getDataBtn = findViewById(R.id.getDataBtn);
        imageButtonPrev = findViewById(R.id.imageButtonPrev);
        imageButtonNext = findViewById(R.id.imageButtonNext);
        buttonTrue = findViewById(R.id.buttonTrue);
        buttonFalse = findViewById(R.id.buttonFalse);
        questionViewText = findViewById(R.id.questionViewText);
        textViewScore = findViewById(R.id.textViewScore);
        textViewQuestionCounter1 = findViewById(R.id.textViewQuestionCounter1);
        textViewHighestScore = findViewById(R.id.textViewHighestScore);

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

        getNoOfQuestions();
    }

    public void getNoOfQuestions() {
        SharedPreferences sharedPreferences = getSharedPreferences("NOOFQUES", MODE_PRIVATE);
        int noOfQues = sharedPreferences.getInt("noOfQuestions", 10);
        Log.d("noOfQues", "getNoOfQuestions: " + noOfQues);
        editTextNumber.setText(String.valueOf(noOfQues));

        textViewHighestScore.setText("Highest score: " + appPrefs.getHighScore());
    }

    public void saveNoOfQuestions() {
        int noOfQues = Integer.parseInt(editTextNumber.getText().toString());
        SharedPreferences sharedPreferences = getSharedPreferences("NOOFQUES", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("noOfQuestions", noOfQues);

        editor.apply();
    }

    public void getQuestions() {
        saveNoOfQuestions();
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
        if (questionList == null && v.getId() != R.id.getDataBtn) {
            Toast.makeText(MainActivity.this, "Hit Get Questions First!", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
            case R.id.imageButtonPrev:
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                }
                moveToNextQuestion();
                break;
            case R.id.imageButtonNext:
                goToNext();
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

    private void addPoints() {
        scoreCounter++;
        score.setScore(scoreCounter);

        textViewScore.setText("Your score: " + score.getScore() + " out of " + questionList.size());
        appPrefs.saveHighestScore(score.getScore());
    }

    private void checkAnswer(boolean userChoice) {
        boolean isAnswerCorrect = questionList.get(currentQuestionIndex).isAnswer();

        int toastMessageId = 0;
        if (userChoice == isAnswerCorrect) {
            addPoints();
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
                goToNext();
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
                goToNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void goToNext() {
        if ((currentQuestionIndex+1) < questionList.size()) {
            currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
            moveToNextQuestion();
        }
    }

    @Override
    protected void onPause() {
        appPrefs.saveHighestScore(score.getScore());
        super.onPause();
    }
}