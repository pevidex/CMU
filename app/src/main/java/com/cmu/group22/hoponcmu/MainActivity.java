package com.cmu.group22.hoponcmu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ImageView img_currentquiz;
    ImageView img_myquizs;
    ImageView img_msgbox;

    static final String[][] LOCATIONS = new String[][]{
            {"belem tower","res/1.jpg "},
            {"cascais","http://nomanbefore.com/wp-content/uploads/2016/09/Cascais-41-e1476847968281-1024x767.jpg"},
            {"Rossio Square","https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/Pra%C3%A7a_de_D._Pedro_IV.jpg/360px-Pra%C3%A7a_de_D._Pedro_IV.jpg"},
            {"25 de Abril Bridge","https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Ponte_25_de_Abril_Lisboa.jpg/330px-Ponte_25_de_Abril_Lisboa.jpg"},
            {"belem tower","http://www.layoverguide.com/wp-content/uploads/2011/05/Belem-tower-in-Lisbon-Portugal.jpg"},
            {"cascais","http://nomanbefore.com/wp-content/uploads/2016/09/Cascais-41-e1476847968281-1024x767.jpg"},
            {"Rossio Square","https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/Pra%C3%A7a_de_D._Pedro_IV.jpg/360px-Pra%C3%A7a_de_D._Pedro_IV.jpg"},
            {"25 de Abril Bridge","https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Ponte_25_de_Abril_Lisboa.jpg/330px-Ponte_25_de_Abril_Lisboa.jpg"},
            {"belem tower","http://www.layoverguide.com/wp-content/uploads/2011/05/Belem-tower-in-Lisbon-Portugal.jpg"},
            {"cascais","http://nomanbefore.com/wp-content/uploads/2016/09/Cascais-41-e1476847968281-1024x767.jpg"},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO:check the cookie if the user has logged in. if not, go to login activity
        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);

        //init the click actions about current_quiz, my quiz, my message box
        img_currentquiz = (ImageView) findViewById(R.id.imageView_currentquiz);
        img_myquizs = (ImageView) findViewById(R.id.imageView_myquizs);
        img_msgbox = (ImageView) findViewById(R.id.imageView_msgbox);

        img_currentquiz.setClickable(true);//TODO:according to the detect of wifi to define whether the user can answer now
        img_currentquiz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CurrentQuizActivity.class);
                startActivity(intent);
            }
        });

        img_myquizs.setClickable(true);
        img_myquizs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);//TODO:change me
                startActivity(intent);
            }
        });
        img_msgbox.setClickable(true);
        img_msgbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);//TODO:change me
                startActivity(intent);
            }
        });

        //init the list of locations
        listView = (ListView) findViewById(R.id.listView_locationslist);
        listView.setAdapter(new InitLocations(this, LOCATIONS));

        //TODO: maybe there should be some click actions on locations?

    }

}
