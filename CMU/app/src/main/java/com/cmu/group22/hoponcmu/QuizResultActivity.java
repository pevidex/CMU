package com.cmu.group22.hoponcmu;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cmu.group22.hoponcmu.Task.UserLocationHistoryTask;

public class QuizResultActivity extends AppCompatActivity {
    private String currentLocation = null;

    GlobalContext globalContext;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        globalContext = (GlobalContext) getApplicationContext();
        String locationName=getIntent().getStringExtra("location");
        new UserLocationHistoryTask(QuizResultActivity.this).execute(globalContext.getUserName(),locationName);
        Log.d("ATLAS",locationName);
    }
}
