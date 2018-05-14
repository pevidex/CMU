package com.cmu.group22.hoponcmu;

import android.app.Application;

import classes.Answers;

public class GlobalContext extends Application {

    private int sessionId = 0;

    private String userName;
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


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
