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

import com.cmu.group22.hoponcmu.Task.UserLocationHistoryTask;
import com.cmu.group22.hoponcmu.WifiDirect.WifiDirectService;

public class QuizResultActivity extends AppCompatActivity {

    ListView listView;

    private String currentLocation = null;
    private List<Question> questions = new ArrayList<Question>();
    private List<Boolean> userResult= new ArrayList<Boolean>();
    private List<Integer> userAnswers= new ArrayList<Integer>();

    GlobalContext globalContext;
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ATLAS","start quizresult");

        super.onCreate(savedInstanceState);
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

        //Get Termite Info
        WifiDirectService wservice;
        boolean isNearMonument = false;
        if(WifiDirectService.isRunning()) {
            wservice = WifiDirectService.getInstance();
            currentLocation = wservice.getMonumentId();
            isNearMonument = wservice.isNearMonument();
        }

        UserLocationHistoryTask u = (UserLocationHistoryTask) new UserLocationHistoryTask(QuizResultActivity.this).execute(globalContext.getUserName(),name_of_quiz);

        try{
            String temp = u.get();}
        catch(Exception e){Log.d("QuizResultActivity","task error");}

        TextView title = (TextView) findViewById(R.id.AnswerResultTItle);
        title.setText(currentLocation);
        //set the number of correct answers and the question the user have answered
        //TODO: not sure it should be counted in server side or client side
        TextView numberOfanswered = (TextView) findViewById(R.id.NumOfAnswered);
        numberOfanswered.setText("you have answered ** questions");
        TextView numberOfcorrect = (TextView) findViewById(R.id.NumOfCorrectAns);
        numberOfcorrect.setText("and ** is correct!");
        //TODO: the share button might be disabled or hidden when user arrives next monument

        listView = (ListView) findViewById(R.id.AnswersResult);
        listView.setAdapter(new InitAnswers(this,questions,userResult,userAnswers));

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
