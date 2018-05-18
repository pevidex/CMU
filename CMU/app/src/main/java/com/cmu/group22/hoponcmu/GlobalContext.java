package com.cmu.group22.hoponcmu;

import android.app.Application;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import classes.Answers;

public class GlobalContext extends Application {

    private int sessionId = 0;

    private String userName;
    private Answers ans = new Answers();

    private PrivateKey signPri;
    private PublicKey signPub;



    private SecretKeySpec secretKey;

    public void setSecretKey(SecretKeySpec SK) { this.secretKey = SK; }

    public SecretKeySpec getSecretKey() {
        return secretKey;
    }

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

    public PrivateKey getSignPri() {
        return signPri;
    }

    public void setSignPri(PrivateKey signPri) {
        this.signPri = signPri;
    }

    public PublicKey getSignPub() {
        return signPub;
    }

    public void setSignPub(PublicKey signPub) {
        this.signPub = signPub;
    }
}
