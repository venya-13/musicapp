package com.example.musicapp;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;

public class RecordOwnSong extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    private ImageView startRecordButton, resetRecord, finishRecord;
    private EditText finalSongName;
    private SeekBar voiceSeekBar, songSeekBar;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_own_song);
        checkMicro();

        if(mediaPlayer2 != null){
            mediaPlayer2.stop();
            mediaPlayer2.release();
        }

        showPermissionDialog();

        startRecordButton = findViewById(R.id.startRecordButton);
        resetRecord = findViewById(R.id.resetRecord);
        finishRecord = findViewById(R.id.finishRecord);
        finalSongName = findViewById(R.id.finalSongName);
        voiceSeekBar = findViewById(R.id.voiceSeekBar);
        songSeekBar = findViewById(R.id.songSeekBar);

        Uri uri = TransmissionInformation.getInstance().getUri();

        mediaRecorder = new MediaRecorder();
        mediaPlayer2 = MediaPlayer.create(getApplicationContext(), uri);

        resetRecord.setVisibility(View.GONE);
        finishRecord.setVisibility(View.GONE);

        startRecordButton.setOnClickListener(v ->{
            startRecordButton.setVisibility(View.GONE);
            resetRecord.setVisibility(View.VISIBLE);
            finishRecord.setVisibility(View.VISIBLE);
            startRecordWithMusic();
        });

        resetRecord.setOnClickListener(v ->{
            resetRecord.setVisibility(View.GONE);
            startRecordButton.setVisibility(View.VISIBLE);
            finishRecord.setVisibility(View.GONE);
            stopRecordVoiceWithMusic();
        });
        finishRecord.setOnClickListener(v ->{
            finishRecord();
        });

        mediaPlayer2.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer arg0)
    {
        finishRecord();
    }


    public void finishRecord (){
        String songName = finalSongName.getText().toString();
        float voiceVolume = (voiceSeekBar.getProgress()) / 10;
        float songVolume = (songSeekBar.getProgress()) / 10;
        TransmissionInformation.getInstance().setSongName(songName);
        TransmissionInformation.getInstance().setVolumeVoice(voiceVolume);
        TransmissionInformation.getInstance().setVolumeSong(songVolume);
        mediaPlayer2.stop();
        mediaRecorder.stop();
        Intent intent = new Intent(RecordOwnSong.this, ListenFinalSong.class);
        startActivity(intent);
    }

    public void startRecordWithMusic (){

        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
            File recordFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            mediaRecorder.setOutputFile(recordFile);

            mediaPlayer2.start();

            String recordVoice = recordFile.getPath();

            TransmissionInformation.getInstance().setString(recordVoice);
        }
        catch (Exception exception){
            exception.printStackTrace();
            return;
        }

    }

    public void stopRecordVoiceWithMusic (){
        mediaRecorder.stop();
        mediaRecorder.reset();

        mediaPlayer2.pause();
        mediaPlayer2.seekTo(0);

        Toast.makeText(this, "Recording is stop", Toast.LENGTH_LONG).show();
    }

    private String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "recordingFile" + ".mp3");

        String path = file.getPath();

        TransmissionInformation.getInstance().setFile(file);
        TransmissionInformation.getInstance().setString2(path);


        return file.getPath();
    }

    private boolean isMicrophonePresent(){
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }
        else{
            return false;
        }
    }

    private void checkMicro (){
        if (isMicrophonePresent() == true){
            Toast.makeText(this, "Micro is true", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Micro is false", Toast.LENGTH_LONG).show();
        }
    }

    public String createTime (int duration){
        String time = "";
        int minutes = duration/1000/60;
        int seconds = duration/1000%60;

        time += minutes+":";

        if (seconds < 10){
            time += "0";
        }
        time += seconds;

        return time;
    }

    private void showPermissionDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.permission_dialog_put_on_headphones);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ImageView button;
        button = dialog.findViewById(R.id.button);

        button.setOnClickListener(v -> {
            dialog.cancel();
        });

        dialog.show();
    }


}