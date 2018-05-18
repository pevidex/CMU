package com.cmu.group22.hoponcmu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.group22.hoponcmu.Task.LoginTask;
import com.cmu.group22.hoponcmu.WifiDirect.WifiDirectService;

public class LoginActivity extends AppCompatActivity {

    GlobalContext globalContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btLogin = (Button) findViewById(R.id.btLogin);
        TextView btRegister = (TextView) findViewById(R.id.btRegister);


        //START WIFI DIRECT SERVICE
        //Start WIFI DIRECT
        if (!WifiDirectService.isRunning()) {
            startWifiService();
        }



        globalContext = (GlobalContext) getApplicationContext();
        Log.d("LoginActivity", "Session ID=" + globalContext.getSessionId());
        if(globalContext.getSessionId() != 0)
            nextActivity();

        btRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), RegisterActivity.class);
                finish();
                startActivity(myIntent);
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText userName = (EditText) findViewById(R.id.etUsername);
                EditText code = (EditText) findViewById(R.id.etCode);
                globalContext.setUserName(userName.getText().toString());
                new LoginTask(LoginActivity.this).execute(userName.getText().toString(),code.getText().toString());
            }
        });

    }
    public void updateInterface(String reply) {
        Toast.makeText(LoginActivity.this, reply,
                Toast.LENGTH_LONG).show();
    }


    public void nextActivity(){
        Intent myIntent = new Intent(this, MainActivity.class);
        finish();
        startActivity(myIntent);
    }

    public void startWifiService(){
        new Thread() {
            public void run() {
                Log.d("WIFI-SERVICE", "STARTING INTENT");
                Intent i = new Intent(getApplicationContext(), WifiDirectService.class);
                startService(i);
            }
        }.start();
    }
}
