package com.cmu.group22.hoponcmu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import classes.Question;

import com.cmu.group22.hoponcmu.Task.UserLocationHistoryTask;

public class QuizResultActivity extends AppCompatActivity {

    ListView listView;

    private String currentLocation = null;
    private List<Question> questions = new ArrayList<Question>();
    private List<Boolean> userResult= new ArrayList<Boolean>();
    private List<Integer> userAnswers= new ArrayList<Integer>();

    GlobalContext globalContext;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        globalContext = (GlobalContext) getApplicationContext();
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
        UserLocationHistoryTask u = (UserLocationHistoryTask) new UserLocationHistoryTask(QuizResultActivity.this).execute(globalContext.getUserName(),name_of_quiz);
        try{
            String temp = u.get();}
        catch(Exception e){Log.d("QuizResultActivity","task error");}

        Log.d("ATLAS","current quizz "+name_of_quiz);
        listView = (ListView) findViewById(R.id.AnswersResult);

        setMyAnswers();
    }

    private void setMyAnswers() {
        //for debug
        /*questionList.add(new Question())
        questions.add(new Question("body1","c1","c2","c3","c4"));
        questions.add(new Question("body2","c1","c2","c3","c4"));
        questions.add(new Question("body3","c1","c2","c3","c4"));*/
        listView.setAdapter(new InitAnswers(this,this.questions));



    }
    public void updateInterface(ArrayList<Question> questions, ArrayList<Boolean> userResult, ArrayList<Integer> userAnswers){
        Log.d("Ricardo","questions size: "+questions.size());
        if(questions!=null)
            this.questions=questions;
        if(userResult!=null)
            this.userResult=userResult;
        if(userAnswers!=null)
            this.userAnswers=userAnswers;

    }


}
