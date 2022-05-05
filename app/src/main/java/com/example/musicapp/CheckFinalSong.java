package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class CheckFinalSong extends AppCompatActivity {

    private Button stopButton,playButton,goBackButton;
    private ImageView addSongButton;
    private TextView songNameTxt;

    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaVoicePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_song);

        stopButton = findViewById(R.id.stopButton);
        playButton = findViewById(R.id.playButton);
        songNameTxt = findViewById(R.id.songNameTxt);
        addSongButton = findViewById(R.id.addSongButton);
        goBackButton = findViewById(R.id.goBackButton);
//
//        File recordedVoice = TransmissionInformation.getInstance().getFile();
//        String recordVoicePath = recordedVoice.toURI().toString();
//        Uri voiceRecordUri = Uri.parse(recordVoicePath);
//        String finalSongName = TransmissionInformation.getInstance().getSongName();
//        Uri musicUri = TransmissionInformation.getInstance().getUri();
//
//        if(mediaPlayer != null){
//            mediaPlayer.stop();
//            mediaPlayer.release();
//        }
//
//        if(mediaVoicePlayer != null){
//            mediaVoicePlayer.stop();
//            mediaVoicePlayer.release();
//        }
//
//        songNameTxt.setText(finalSongName);
//
//        mediaPlayer = MediaPlayer.create(getApplicationContext(), musicUri);
//        mediaPlayer.start();
//        mediaVoicePlayer = MediaPlayer.create(getApplicationContext(), voiceRecordUri);
//        mediaVoicePlayer.start();
//        playButton.setVisibility(View.GONE);
//
//        playButton.setOnClickListener(v -> {
//            mediaPlayer.start();
//            mediaVoicePlayer.start();
//            stopButton.setVisibility(View.VISIBLE);
//            playButton.setVisibility(View.GONE);
//        });
//        stopButton.setOnClickListener(v -> {
//            mediaPlayer.pause();
//            mediaVoicePlayer.pause();
//            stopButton.setVisibility(View.GONE);
//            playButton.setVisibility(View.VISIBLE);
//        });
//
//
//        addSongButton.setOnClickListener(v ->{
//            Intent intent = new Intent(CheckFinalSong.this, MergeFiles.class);
//            startActivity(intent);
//            mediaPlayer.pause();
//            mediaVoicePlayer.pause();
//        });
//
////        goBackButton.setOnClickListener(v ->{
////            Intent intent = new Intent(CheckFinalSong.this, RecordOwnSong.class);
////            startActivity(intent);
////            mediaPlayer.pause();
////            mediaVoicePlayer.pause();
////        });

    }
}