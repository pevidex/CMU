package com.cmu.group22.hoponcmu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CurrentQuizActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentquiz);
        TextView ssid = (TextView) findViewById (R.id.QuizTitle);

        ssid.setText("Cascais");
    }
}
