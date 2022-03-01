package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button youtubeMusicButton,ownMusicButton, requestPermissionButton;
    private static final int permissionsRequestCodeRecord = 1;
    private static final int permissionsRequestCodeOwnMusic = 2;
    private int grantedPermission = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        youtubeMusicButton = findViewById(R.id.youtubeMusicButton);
        ownMusicButton = findViewById(R.id.ownMusicButton);
        requestPermissionButton = findViewById(R.id.requestPermissionButton);

        youtubeMusicButton.setVisibility(View.GONE);
        ownMusicButton.setVisibility(View.GONE);

        checkPermission();


        youtubeMusicButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, YoutubeSearch.class);
                startActivity(intent);
        });

        ownMusicButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ownMusic.class);
                startActivity(intent);
        });

        requestPermissionButton.setOnClickListener(v -> {
            checkPermission();
        });

    }


    private void checkPermission() {
        int grantedPermission = 0;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            grantedPermission += 1;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, permissionsRequestCodeRecord);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            grantedPermission += 1;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permissionsRequestCodeOwnMusic);
        }

        if (grantedPermission == 2){
            ownMusicButton.setVisibility(View.VISIBLE);
            youtubeMusicButton.setVisibility(View.VISIBLE);
            requestPermissionButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int grantedPermission2 = 0;
        switch (requestCode) {

            case permissionsRequestCodeRecord:

                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        showPermissionDialog();
                    } else{
                        grantedPermission2 += 1;
                    }
                }
                return;

            case permissionsRequestCodeOwnMusic:

                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        showPermissionDialog();
                    } else{
                        grantedPermission2 += 1;
                    }
                }
                return;
        }
        if (grantedPermission2 == 2){
            ownMusicButton.setVisibility(View.VISIBLE);
            youtubeMusicButton.setVisibility(View.VISIBLE);
            requestPermissionButton.setVisibility(View.GONE);
        }

    }

    private void showPermissionDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.permission_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ImageView button;
        button = dialog.findViewById(R.id.button);

        button.setOnClickListener(v -> {
                dialog.cancel();
        });


        dialog.show();
    }
}