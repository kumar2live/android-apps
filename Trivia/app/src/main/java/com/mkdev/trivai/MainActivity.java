package com.mkdev.trivai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mkdev.trivai.controller.AppController;
import com.mkdev.trivai.data.QuestionBank;

public class MainActivity extends AppCompatActivity {
    private Button getDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDataBtn = findViewById(R.id.getDataBtn);

        getDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new QuestionBank().getQuestions();
            }
        });


    }
}