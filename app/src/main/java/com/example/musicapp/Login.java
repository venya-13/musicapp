package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        mAuth = FirebaseAuth.getInstance();

        appName = findViewById(R.id.appName);
        registerButton = findViewById(R.id.registerButton);
        forgetPasswordButton = findViewById(R.id.forgetPasswordButton);
        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        forgetPasswordButton.setOnClickListener(this);

        connectionChecker();
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
            case R.id.forgetPasswordButton:
                startActivity(new Intent(this, ForgetPassword.class));
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

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            progressBar.setVisibility(View.VISIBLE);
            if(task.isSuccessful()){
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
            } else{
                Toast.makeText(Login.this, "Failed to login!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                connectionChecker();
            }
        });
    }



    private void showInternetConnectionDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.internet_connection_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ImageView button;
        button = dialog.findViewById(R.id.button);

        button.setOnClickListener(v -> {
            dialog.cancel();
        });

        dialog.show();
    }

    private void connectionChecker() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Log.d("Internet", "true");
        } else {
            showInternetConnectionDialog();
        }
    }
}