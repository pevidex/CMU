package com.cmu.group22.hoponcmu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class QuizResultActivity extends AppCompatActivity {
    private String currentLocation = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int i=getIntent().getIntExtra("index_of_quiz",0);
        Log.d("ATLAS",i+"");
    }
}
