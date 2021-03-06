package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView appName;
    private EditText passwordTxt, emailTxt;
    private ProgressBar progressBar;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        appName = findViewById(R.id.appName);
        passwordTxt = findViewById(R.id.passwordTxt);
        emailTxt = findViewById(R.id.emailTxt);
        progressBar = findViewById(R.id.progressBar);
        registerButton = findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();

        appName.setOnClickListener(this);
        registerButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.appName:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.registerButton:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        String email = emailTxt.getText().toString().trim();
        String password = passwordTxt.getText().toString().trim();

        if (email.isEmpty()){
            emailTxt.setError("Email is required");
            emailTxt.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTxt.setError("Please provide valid Email");
            emailTxt.requestFocus();
            return;
        }

        if (password.isEmpty()){
            passwordTxt.setError("Password is required");
            passwordTxt.requestFocus();
            return;
        }
        if (password.length() < 6){
            passwordTxt.setError("Minimum password length should be 6 character");
            passwordTxt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    Log.d("Register Successful", "Successful");
                    if (task.isSuccessful()){
                        User user = new User(email);

                        db.collection("Users")
                                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                .set(user).addOnCompleteListener(task1 -> {

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    sendEmailVerificationDialog();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, "Something go wrong", Toast.LENGTH_SHORT).show();
                                }
                            });

                                    if (task1.isSuccessful()){
                                        Toast.makeText(Register.this, "User has benn registered successfully",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        Intent intent = new Intent(Register.this, Login.class);
                                        startActivity(intent);
                                    } else{
                                        Toast.makeText(Register.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);

                                        connectionChecker();
                                    }
                                });

                    } else {
                        Toast.makeText(Register.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        connectionChecker();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Register class", "onFailure: "+ e.toString());
            }
        });
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

    private void sendEmailVerificationDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.send_email_verifiaction_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ImageView button;
        button = dialog.findViewById(R.id.button);

        button.setOnClickListener(v -> {
            dialog.cancel();
        });

        dialog.show();
    }

}