package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;

public class FinalActivity extends AppCompatActivity {

    private Button downloadTrack, shareButton, backToMainPageButton;
    private TextView logoTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        Uri songUri = TransmissionInformation.getInstance().getAlreadyRecordedUri();
        String trackName = TransmissionInformation.getInstance().getAlreadyRecordedTrackName();
        String outputPath = songUri.getPath().toString();

        downloadTrack = findViewById(R.id.downloadTrack);
        shareButton = findViewById(R.id.shareButton);
        backToMainPageButton = findViewById(R.id.backToMainPageButton);
        logoTxt = findViewById(R.id.logoTxt);


        logoTxt.setOnClickListener(v -> {
            Intent intent = new Intent(FinalActivity.this, MainActivity.class);
            startActivity(intent);
        });

        backToMainPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(FinalActivity.this, MainActivity.class);
            startActivity(intent);
        });

        shareButton.setOnClickListener(v -> {
            File recordedSong = new File(outputPath);
            Uri resUri = FileProvider.getUriForFile(this, "com.example.musicapp", recordedSong);
            try {
                shareDialog(resUri);
            } catch (Exception ex) {
                Toast.makeText(this, "Fail to share fail!", Toast.LENGTH_SHORT).show();
                Log.e("Share", ex.getMessage(), ex);
            }

        });

        downloadTrack.setOnClickListener(v -> {
            downloadTrack(trackName,outputPath);
        });

    }

    private void downloadTrack(String finalSongName,String outputPath){
        String downloadsPathString = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + finalSongName;

        Path trackPath = Paths.get(outputPath);

        try {
            if(!trackPath.toFile().exists()) {
                Log.v("COPY_FILE", "Copy file failed. Source file missing.");
                return;
            }

            InputStream in = new FileInputStream(outputPath);
            OutputStream out = new FileOutputStream(downloadsPathString);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();

            Log.v("COPY_FILE", "Copy file successful.");

            Toast.makeText(this, "your track has been downloaded to the music directory!", Toast.LENGTH_SHORT).show();
        }catch (Exception exception){
            Log.e("Download error !!!!!", exception.getMessage());
        }
    }

    private void shareDialog(Uri uri) throws Exception{
        Intent shareIntent = new Intent(Intent.ACTION_SEND); // create action send
        shareIntent.setType("audio/*");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "Share File"));
    };
}