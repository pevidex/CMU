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

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

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

        //debug
        // List all security providers
        for (Provider p : Security.getProviders()) {
            Log.d("provider", String.format("== %s ==", p.getName()));
            for (Provider.Service s : p.getServices()) {
                Log.d("provider", String.format("- %s", s.getAlgorithm()));
            }
        }
//        try {
//            SignKeypair  signKeypair = new SignKeypair();
//            RegisterCommand lc = new RegisterCommand("code","name", signKeypair.getSignPbkBytes());
//            SignedObject lc_signed= new SignedObject(lc, signKeypair);
//            RegisterCommand deser_lc = (RegisterCommand) lc_signed.getObject();
//            Log.d("debug",deser_lc.getUsername());
//
//            if(lc_signed.verify(deser_lc.getPubkey()))
//                Log.d("debug","success");
//
//            //SecretKey sk = globalContext.getSecretKey();
//            //EncryptedObject pack = new EncryptedObject(lc_signed, sk);
//            //Log.d("debug2",((RegisterCommand) pack.decrypt(sk).getObject()).getUsername());
//
//
//        } catch (NoSuchProviderException e) {
//            Log.d("errorx",e.getMessage());
//        } catch (IOException e) {
//            Log.d("errorx",e.getMessage());
//        } catch (ClassNotFoundException e) {
//            Log.d("errorx",e.getMessage());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            Log.d("errorx",e.getMessage());
//        } catch (SignatureException e) {
//            Log.d("errorx",e.getMessage());
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            Log.d("errorx",e.getMessage());
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            Log.d("errorx",e.getMessage());
//            e.printStackTrace();
//        }
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
            }
        });

        //Get Locations From Server
        new GetLocationsTask(MainActivity.this).execute();

        img_currentquiz.setClickable(true);//TODO:according to the detect of wifi to define whether the user can answer now also should consider about whether the user has answered this question
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
                Intent intent = new Intent(v.getContext(), MyQuizActivity.class);//TODO:change me
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
