package com.cmu.group22.hoponcmu;

import android.app.Application;

public class GlobalContext extends Application {

    private int sessionId = 0;

    public int getSessionId(){
        return this.sessionId;
    }

    public void setSessionId(int id){
        this.sessionId = id;
    }
}
