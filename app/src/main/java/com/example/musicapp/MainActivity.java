package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    private Button youtubeMusicButton, ownMusicButton, requestPermissionButton;
    private TextView logoutTxt;
    private SpotsDialog dialog;

    private String[] PERMISSIONS;
    private final int RequestCode = 1;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        youtubeMusicButton = findViewById(R.id.youtubeMusicButton);
        ownMusicButton = findViewById(R.id.ownMusicButton);
        requestPermissionButton = findViewById(R.id.requestPermissionButton);
        logoutTxt = findViewById(R.id.logoutTxt);

        PERMISSIONS = new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        youtubeMusicButton.setVisibility(View.GONE);
        ownMusicButton.setVisibility(View.GONE);
        requestPermissionButton.setVisibility(View.VISIBLE);

        requestPermission();

        youtubeMusicButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AlreadyRecordedTracks.class);
            startActivity(intent);
        });

        ownMusicButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SplashScreen.class);
            startActivity(intent);
            dialog = new SpotsDialog(this,"");
            dialog.show();
        });

        requestPermissionButton.setOnClickListener(v -> {
            requestPermission();
        });

        logoutTxt.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        });


    }

    private void requestPermission(){
        if (!hasPermissions(MainActivity.this,PERMISSIONS)){
            ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS,RequestCode);
        } else {
            requestPermissionButton.setVisibility(View.GONE);
            youtubeMusicButton.setVisibility(View.VISIBLE);
            ownMusicButton.setVisibility(View.VISIBLE);
        }
    }


    private boolean hasPermissions(Context context, String... PERMISSIONS) {
        if(context != null && PERMISSIONS != null){

            for (String permission: PERMISSIONS){
                if (ActivityCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                } else {
                    requestPermissionButton.setVisibility(View.GONE);
                    youtubeMusicButton.setVisibility(View.VISIBLE);
                    ownMusicButton.setVisibility(View.VISIBLE);
                }
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionsAmount = 0;
        if(requestCode == RequestCode){
            for(int res: grantResults) {
                if (res == PackageManager.PERMISSION_GRANTED) {
                    permissionsAmount++;
                } else {
                    showPermissionDialog();
                }
            }
        }
        if (permissionsAmount == grantResults.length){
            requestPermission();
        }

    }


    private void showPermissionDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.permission_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ImageView button;
        button = dialog.findViewById(R.id.button);

        button.setOnClickListener(v -> {
                dialog.dismiss();
        });

        dialog.show();
    }
}