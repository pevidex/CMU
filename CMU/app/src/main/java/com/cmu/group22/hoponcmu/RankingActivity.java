package com.cmu.group22.hoponcmu;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Map;

public class RankingActivity extends AppCompatActivity {

    TextView tvRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        tvRanking = (TextView) findViewById(R.id.textView_ranking);

        GlobalContext globalContext = (GlobalContext) getApplicationContext();
        Map<String, Map<String, Integer>> ranking = globalContext.getRanking();
        Map<String, Map<String, Long>> rankingTime = globalContext.getRankingTime();
        for(String s : ranking.keySet()){
            for(String loc : ranking.get(s).keySet()) {
                tvRanking.setText(tvRanking.getText().toString() + "User: " + s + "\n\tLocation: " + loc +
                        "\n\t\tAnswers Right: " + ranking.get(s).get(loc) +
                        "\n\t\tQuizz Time: " + rankingTime.get(s).get(loc)/1000);
            }
        }
    }
}
