package com.cmu.group22.hoponcmu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import classes.Question;

public class QuizResultActivity extends AppCompatActivity {

    ListView listView;

    private String currentLocation = null;
    private List<Question> questions = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizresult);

        String name_of_quiz;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            name_of_quiz = bundle.getString("name_of_quiz");
        }else{
            return;
        }
        currentLocation = name_of_quiz;

        TextView title = (TextView) findViewById(R.id.AnswerResultTItle);
        title.setText(currentLocation);

        listView = (ListView) findViewById(R.id.AnswersResult);

        setMyAnswers(null);
    }

    private void setMyAnswers(List<Question> questionList) {
        //for debug
        //questionList.add(new Question())
        questions.add(new Question("body1","c1","c2","c3","c4"));
        questions.add(new Question("body2","c1","c2","c3","c4"));
        questions.add(new Question("body3","c1","c2","c3","c4"));
        listView.setAdapter(new InitAnswers(this,questions));



    }


}
