package com.cmu.group22.hoponcmu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.group22.hoponcmu.Task.LoginTask;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btLogin = (Button) findViewById(R.id.btLogin);
        TextView btRegister = (TextView) findViewById(R.id.btRegister);

        final GlobalContext globalContext = (GlobalContext) getApplicationContext();
        globalContext.getSessionId();


        btRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), RegisterActivity.class);
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
        startActivity(myIntent);
        finish();
    }
}
