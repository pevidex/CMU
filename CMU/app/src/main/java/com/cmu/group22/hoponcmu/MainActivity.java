package com.cmu.group22.hoponcmu;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cmu.group22.hoponcmu.Task.GetLocationsTask;
import com.cmu.group22.hoponcmu.WifiDirect.WifiDirectService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Map;

import classes.Location;
import command.RegisterCommand;
import security.SignKeypair;
import security.SignedObject;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ImageView img_currentquiz;
    ImageView img_myquizs;
    ImageView img_msgbox;

    ArrayList<Location> locations=null;

    Button btLogin;
    Button btLogout;

    final private Integer CLEAR_SESSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GlobalContext globalContext = (GlobalContext) getApplicationContext();
        Log.d("MainActivity ",  Integer.toString(globalContext.getSessionId()));
        //TODO:check the cookie if the user has logged in. if not, go to login activity
        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);


        //init the click actions about logout, current_quiz, my quiz, my message box
        btLogout = (Button) findViewById(R.id.Btn_logout);
        img_currentquiz = (ImageView) findViewById(R.id.imageView_currentquiz);
        img_myquizs = (ImageView) findViewById(R.id.imageView_myquizs);
        img_msgbox = (ImageView) findViewById(R.id.imageView_msgbox);
        listView = (ListView) findViewById(R.id.listView_locationslist);

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear session and redirect to login page
                globalContext.setSessionId(CLEAR_SESSION);
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //Get Locations From Server
        new GetLocationsTask(MainActivity.this, globalContext).execute();

        img_currentquiz.setClickable(true);//TODO:according to the detect of wifi to define whether the user can answer now also should consider about whether the user has answered this question
        img_currentquiz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(globalContext);

                Intent intent = new Intent(v.getContext(), CurrentQuizActivity.class);


                //CHECK IF DEVICE DETECTS WIFI MONUMENT
                String currentLocation = getCurrentLocation();

                intent.putExtra("location", currentLocation);//need to know which location we're at

                startActivity(intent);
            }
        });

        img_myquizs.setClickable(true);
        img_myquizs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyQuizActivity.class);//TODO:change me
                startActivity(intent);
            }
        });
        img_msgbox.setClickable(true);
        img_msgbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RankingActivity.class);
                startActivity(intent);
            }
        });

        //TODO: maybe there should be some click actions on locations?

    }
    @Override
    public void onPause() {
        super.onPause();
        //TODO
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


    public String getCurrentLocation(){
        String currentLocation = "";
        WifiDirectService wifiService;
        if(WifiDirectService.isRunning()) {
            wifiService = WifiDirectService.getInstance();
            currentLocation = wifiService.getMonumentId();

        }
        return currentLocation;
    }
}
