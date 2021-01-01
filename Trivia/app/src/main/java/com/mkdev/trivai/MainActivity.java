package com.mkdev.trivai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
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
                break;
            case R.id.buttonFalse:
                checkAnswer(false);
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
        } else {
            toastMessageId = R.string.wrong_answer;
        }

        Toast.makeText(MainActivity.this, toastMessageId, Toast.LENGTH_SHORT).show();
    }
}