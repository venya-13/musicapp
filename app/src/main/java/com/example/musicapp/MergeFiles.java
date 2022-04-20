package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import java.io.File;

public class MergeFiles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_files);

        Uri uri = transmissionInformation.getInstance().getUri();
        File recordedVoice = transmissionInformation.getInstance().getFile();
    }
}