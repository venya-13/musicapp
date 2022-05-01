package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class CheckSongActivity extends AppCompatActivity {

    private Button stopButton, playButton, addSongButton, goBackButton, skipSong;
    private TextView startTxt, endTxt, songNameTxt;
    private SeekBar seekBar;

    String someName;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread upDateSeekBar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_song);

        stopButton = findViewById(R.id.stopButton);
        playButton = findViewById(R.id.playButton);
        startTxt = findViewById(R.id.startTxt);
        endTxt = findViewById(R.id.endTxt);
        songNameTxt = findViewById(R.id.songNameTxt);
        seekBar = findViewById(R.id.seekBar);
        addSongButton = findViewById(R.id.addSongButton);
        skipSong = findViewById(R.id.skipSong);
        goBackButton = findViewById(R.id.goBackButton);

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }


        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songName");
        position = bundle.getInt("position", 0);
        songNameTxt.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(position).toString());
        someName = mySongs.get(position).getName();
        songNameTxt.setText(someName);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                startTxt.setText(currentTime);
                handler.postDelayed(this,delay);
            }
        }, delay);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });


        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        playButton.setVisibility(View.GONE);

        int timeLength = (mediaPlayer.getDuration()) * 1000;
        TransmissionInformation.getInstance().setNumber(timeLength);

        String endTime = createTime(mediaPlayer.getDuration());
        endTxt.setText(endTime);

        upDateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;

                while(currentPosition<totalDuration){
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }
                    catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        seekBar.setMax(mediaPlayer.getDuration());
        upDateSeekBar.start();

        playButton.setOnClickListener(v -> {
            mediaPlayer.start();
            stopButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.GONE);
        });
        stopButton.setOnClickListener(v -> {
            mediaPlayer.pause();
            stopButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
        });

        skipSong.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position+1)%mySongs.size());
            Uri u = Uri.parse(mySongs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
            someName = mySongs.get(position).getName();
            songNameTxt.setText(someName);
            mediaPlayer.start();
            stopButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.GONE);
            String timeEnd = createTime(mediaPlayer.getDuration());
            endTxt.setText(timeEnd);

        });

        goBackButton.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position-1)%mySongs.size());
            Uri u = Uri.parse(mySongs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
            someName = mySongs.get(position).getName();
            songNameTxt.setText(someName);
            mediaPlayer.start();
            stopButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.GONE);
            String timeEnd = createTime(mediaPlayer.getDuration());
            endTxt.setText(timeEnd);
        });

        addSongButton.setOnClickListener(v ->{
            Intent intent = new Intent(CheckSongActivity.this, RecordOwnSong.class);
            startActivity(intent);
            mediaPlayer.pause();
        });

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
}