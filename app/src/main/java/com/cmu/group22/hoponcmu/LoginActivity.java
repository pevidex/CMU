package com.cmu.group22.hoponcmu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {

    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
/*
        btLogin = (Button) findViewById(R.id.btLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText userName = (EditText) findViewById(R.id.etUsername);
                EditText code = (EditText) findViewById(R.id.etCode);
                //new LoginTask(LoginActivity.this).execute(userName.getText().toString(),code.getText().toString());
            }
        });
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
*/
    }
}
