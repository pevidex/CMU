package com.cmu.group22.hoponcmu;

import android.app.Application;

import classes.Answers;

public class GlobalContext extends Application {

    private int sessionId = 0;

    private Answers ans = new Answers();

    public int getSessionId(){
        return this.sessionId;
    }

    public void setSessionId(int id){
        this.sessionId = id;
    }

    public Answers getAnswers(){ return this.ans; }

    public void setAnswers(Answers a){
        ans = a;
    }

}
