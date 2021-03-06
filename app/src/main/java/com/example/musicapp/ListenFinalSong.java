package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.PresetReverb;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class ListenFinalSong extends AppCompatActivity {

    private Button stopButton,playButton,goBackButton;
    private ImageView mergeImageView;
    private TextView songNameTxt;

    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaVoicePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_final_song);

        stopButton = findViewById(R.id.stopButton);
        playButton = findViewById(R.id.playButton);
        songNameTxt = findViewById(R.id.songNameTxt);
        mergeImageView = findViewById(R.id.mergeImageView);
        goBackButton = findViewById(R.id.goBackButton);

        File recordedVoice = TransmissionInformation.getInstance().getFile();
        String recordVoicePath = recordedVoice.toString();
        Uri voiceRecordUri = Uri.parse(recordVoicePath);
        String finalSongName = TransmissionInformation.getInstance().getSongName();
        Uri musicUri = TransmissionInformation.getInstance().getUri();
        float voiceVolume = TransmissionInformation.getInstance().getVolumeVoice();
        float songVolume = TransmissionInformation.getInstance().getVolumeSong();

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        if(mediaVoicePlayer != null){
            mediaVoicePlayer.stop();
            mediaVoicePlayer.release();
        }

        songNameTxt.setText(finalSongName);

        mediaVoicePlayer = MediaPlayer.create(getApplicationContext(), voiceRecordUri);
        mediaVoicePlayer.start();

        try {
            Thread.sleep(320);
        } catch (InterruptedException e) {
            Log.e("Time Error!!!!", e.getMessage());
        }

        mediaPlayer = MediaPlayer.create(getApplicationContext(), musicUri);
        mediaPlayer.start();
        playButton.setVisibility(View.GONE);
        mediaVoicePlayer.setVolume(voiceVolume,voiceVolume);
        mediaPlayer.setVolume(songVolume,songVolume);

//        try{
//            PresetReverb mReverb = new PresetReverb(0,0);
//            mReverb.setPreset(PresetReverb.PRESET_LARGEROOM);
//            mReverb.setEnabled(true);
//
//            mediaVoicePlayer.attachAuxEffect(mReverb.getId());
//            mediaVoicePlayer.setAuxEffectSendLevel(1.0f);
//        }catch (Exception e){
//            Log.e("Reverb Error", e.getMessage());
//        }


        playButton.setOnClickListener(v -> {
            mediaPlayer.start();
            mediaVoicePlayer.start();
            stopButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.GONE);
        });

        stopButton.setOnClickListener(v -> {
            mediaPlayer.pause();
            mediaVoicePlayer.pause();
            stopButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
        });

        mergeImageView.setOnClickListener(v ->{
            Intent intent = new Intent(ListenFinalSong.this, MergeFiles.class);
            startActivity(intent);
            mediaPlayer.pause();
            mediaVoicePlayer.pause();
        });

        goBackButton.setOnClickListener(v ->{
            Intent intent = new Intent(ListenFinalSong.this, RecordOwnSong.class);
            startActivity(intent);
            mediaPlayer.pause();
            mediaVoicePlayer.pause();
        });
    }
}