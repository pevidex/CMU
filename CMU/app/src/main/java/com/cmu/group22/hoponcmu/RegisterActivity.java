package com.cmu.group22.hoponcmu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.group22.hoponcmu.Task.RegisterTask;

public class RegisterActivity extends AppCompatActivity {
    GlobalContext globalContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btRegister = (Button) findViewById(R.id.btRegister);
        TextView btLogin = (TextView) findViewById(R.id.btLogin);
        globalContext = (GlobalContext) getApplicationContext();
        btLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();

            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText userName = (EditText) findViewById(R.id.etUsername);
                EditText code = (EditText) findViewById(R.id.etCode);
                globalContext.setUserName(userName.getText().toString());
                new RegisterTask(RegisterActivity.this).execute(userName.getText().toString(),code.getText().toString());
            }
        });
    }
    public void updateInterface(String reply) {
        Toast.makeText(RegisterActivity.this, reply,
                Toast.LENGTH_LONG).show();
    }
    public void nextActivity(){
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
        finish();
    }
}
