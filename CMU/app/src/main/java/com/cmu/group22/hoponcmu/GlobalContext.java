package com.cmu.group22.hoponcmu;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import classes.Answers;
import classes.Question;

public class GlobalContext extends Application {

    private int sessionId = 0;

    private String userName;
    private Answers ans = new Answers();

    ArrayList<Question> quizz = new ArrayList<Question>();

    public int getSessionId(){
        return this.sessionId;
    }

    public void setSessionId(int id){
        this.sessionId = id;
    }

    public Answers getAnswers(){ return this.ans; }

    public ArrayList<Question> getQuizz() {
        return quizz;
    }

    public void setQuizz(ArrayList<Question> quizz) {
        this.quizz = quizz;
    }

    public void setAnswers(Answers a){
        ans = a;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
