package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private Button goBackButton, resetButton;

    private EditText emailTxt;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        goBackButton = findViewById(R.id.goBackButton);
        emailTxt = findViewById(R.id.emailTxt);
        resetButton = findViewById(R.id.resetButton);

        auth = FirebaseAuth.getInstance();

        goBackButton.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
        });

        resetButton.setOnClickListener(v ->{
            resetPassword();
        });
    }

    private void resetPassword() {
        String email = emailTxt.getText().toString().trim();

        if (email.isEmpty()){
            emailTxt.setError("Email is required!");
            emailTxt.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTxt.setError("Please provide a valid email!!");
            emailTxt.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                showPermissionDialog();
            } else {
                Toast.makeText(this, "Try again! Something wrong happened!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPermissionDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.check_email_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ImageView button;
        button = dialog.findViewById(R.id.button);

        button.setOnClickListener(v -> {
            dialog.cancel();
        });

        dialog.show();
    }
}