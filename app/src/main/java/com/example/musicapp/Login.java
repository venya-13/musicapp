package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements  View.OnClickListener {
    private TextView appName, registerButton, forgetPasswordButton;
    private EditText emailTxt, passwordTxt;
    private ProgressBar progressBar;
    private Button loginButton;

    private FirebaseAuth mAuth;

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
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerButton:
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.loginButton:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = emailTxt.getText().toString().trim();
        String password = passwordTxt.getText().toString().trim();

        if (email.isEmpty()){
            emailTxt.setError("Email is required!");
            emailTxt.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTxt.setError("Please enter a valid email!");
            emailTxt.requestFocus();
            return;
        }
        if (password.isEmpty()){
            passwordTxt.setError("Password is required!");
            passwordTxt.requestFocus();
            return;
        }
        if (password.length() < 6){
            passwordTxt.setError("Minimum password length is 6 characters!");
            passwordTxt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            } else{
                Toast.makeText(Login.this, "Failed to login!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}