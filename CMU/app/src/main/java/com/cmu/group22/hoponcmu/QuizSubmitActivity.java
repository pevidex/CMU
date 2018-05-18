package com.cmu.group22.hoponcmu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import classes.Question;

import com.cmu.group22.hoponcmu.Task.UserLocationHistoryTask;
import com.cmu.group22.hoponcmu.WifiDirect.WifiDirectService;

public class QuizSubmitActivity extends AppCompatActivity {

    private String currentLocation = null;

    int correctAnswers;
    long quizzTime;

    GlobalContext globalContext;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalContext = (GlobalContext) getApplicationContext();
        setContentView(R.layout.activity_quizresult);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            correctAnswers = bundle.getInt("correctAnswers");
            quizzTime = bundle.getLong("quizzTime");
            currentLocation = bundle.getString("name_of_quiz");
        }else{
            return;
        }


        TextView title = (TextView) findViewById(R.id.AnswerResultTItle);
        Button btShare = (Button) findViewById(R.id.btShare);
        btShare.setOnClickListener(shareClickListener);
        title.setText(currentLocation);

        TextView numberOfcorrect = (TextView) findViewById(R.id.NumOfCorrectAns);

        numberOfcorrect.setText("You got " + correctAnswers + " answers correct!");
    }

    private View.OnClickListener shareClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("QuizSubmiteActivity", "Start to share answers");
            WifiDirectService.getInstance().shareResults(QuizSubmitActivity.this, correctAnswers, currentLocation, globalContext.getUserName(), quizzTime);
        }
    };
}
