package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Login extends AppCompatActivity implements  View.OnClickListener {
    private TextView appName, registerButton, forgetPasswordButton;
    private EditText emailTxt, passwordTxt;
    private ProgressBar progressBar;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appName = findViewById(R.id.appName);
        registerButton = findViewById(R.id.registerButton);
        forgetPasswordButton = findViewById(R.id.forgetPasswordButton);
        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerButton:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }
}