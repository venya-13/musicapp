package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Button youtubeMusicButton, ownMusicButton, requestPermissionButton;
    private String[] PERMISSIONS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        youtubeMusicButton = findViewById(R.id.youtubeMusicButton);
        ownMusicButton = findViewById(R.id.ownMusicButton);
        requestPermissionButton = findViewById(R.id.requestPermissionButton);

        PERMISSIONS = new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        youtubeMusicButton.setVisibility(View.GONE);
        ownMusicButton.setVisibility(View.GONE);
        requestPermissionButton.setVisibility(View.VISIBLE);

        requestPermission();


        youtubeMusicButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, YoutubeSearch.class);
            startActivity(intent);
        });

        ownMusicButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OwnMusic.class);
            startActivity(intent);
        });

        requestPermissionButton.setOnClickListener(v -> {
            requestPermission();
        });

    }

    private void requestPermission(){
        if (!hasPermissions(MainActivity.this,PERMISSIONS)){
            ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS,1);
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
        if(requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                permissionsAmount += 1;
            }else{
                showPermissionDialog();
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED){
                permissionsAmount += 1;
            }else{
                showPermissionDialog();
            }

        }
        if (permissionsAmount == 2){
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
                dialog.cancel();
        });


        dialog.show();
    }
}