package com.cmu.group22.hoponcmu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cmu.group22.hoponcmu.Task.GetLocationsTask;

import java.util.ArrayList;

import classes.Answers;
import classes.Location;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ImageView img_currentquiz;
    ImageView img_myquizs;
    ImageView img_msgbox;

    //Answers ans = new Answers();

    ArrayList<Location> locations=null;

    /*static final String[][] LOCATIONS = new String[][]{
            {"1belem tower","res/1.jpg "},
            {"2cascais","http://nomanbefore.com/wp-content/uploads/2016/09/Cascais-41-e1476847968281-1024x767.jpg"},
            {"3Rossio Square","https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/Pra%C3%A7a_de_D._Pedro_IV.jpg/360px-Pra%C3%A7a_de_D._Pedro_IV.jpg"},
            {"425 de Abril Bridge","https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Ponte_25_de_Abril_Lisboa.jpg/330px-Ponte_25_de_Abril_Lisboa.jpg"},
            {"5belem tower","http://www.layoverguide.com/wp-content/uploads/2011/05/Belem-tower-in-Lisbon-Portugal.jpg"},
            {"6cascais","http://nomanbefore.com/wp-content/uploads/2016/09/Cascais-41-e1476847968281-1024x767.jpg"},
            {"7Rossio Square","https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/Pra%C3%A7a_de_D._Pedro_IV.jpg/360px-Pra%C3%A7a_de_D._Pedro_IV.jpg"},
            {"825 de Abril Bridge","https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Ponte_25_de_Abril_Lisboa.jpg/330px-Ponte_25_de_Abril_Lisboa.jpg"},
            {"9belem tower","http://www.layoverguide.com/wp-content/uploads/2011/05/Belem-tower-in-Lisbon-Portugal.jpg"},
            {"10cascais","http://nomanbefore.com/wp-content/uploads/2016/09/Cascais-41-e1476847968281-1024x767.jpg"},
    };*/

    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("ATLAS","on create main activity");

        final GlobalContext globalContext = (GlobalContext) getApplicationContext();
        Log.d("MainActivity ",  Integer.toString(globalContext.getSessionId()));
        //TODO:check the cookie if the user has logged in. if not, go to login activity
        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);

        //init the click actions about current_quiz, my quiz, my message box
        img_currentquiz = (ImageView) findViewById(R.id.imageView_currentquiz);
        img_myquizs = (ImageView) findViewById(R.id.imageView_myquizs);
        img_msgbox = (ImageView) findViewById(R.id.imageView_msgbox);
        listView = (ListView) findViewById(R.id.listView_locationslist);

        //Get Locations From Server
        new GetLocationsTask(MainActivity.this).execute();

        img_currentquiz.setClickable(true);//TODO:according to the detect of wifi to define whether the user can answer now
        img_currentquiz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(globalContext);

                Intent intent = new Intent(v.getContext(), CurrentQuizActivity.class);

                intent.putExtra("location","belem tower");//need to know which location we're at

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

        //TODO: maybe there should be some click actions on locations?

    }
    public void updateLocations(ArrayList<Location> locations){
        this.locations=locations;
        //init the list of locations
        listView.setAdapter(new InitLocations(this, locations));
        Log.d("updateLocations", Integer.toString(locations.size()));
    }
    public void updateInterface(String reply) {
        Toast.makeText(MainActivity.this, reply,
                Toast.LENGTH_LONG).show();
    }
}
