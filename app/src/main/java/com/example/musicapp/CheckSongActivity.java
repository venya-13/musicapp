package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class CheckSongActivity extends AppCompatActivity {

    private Button stopButton, playButton;
    private TextView startTxt, endTxt, songNameTxt;
    private SeekBar seekBar;
    //private BarVisualizer visualizer;

    String someName;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;

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
        //visualizer = findViewById(R.id.blast);

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


        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        playButton.setVisibility(View.GONE);

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

    }
}