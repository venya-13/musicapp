package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import dmax.dialog.SpotsDialog;
import zeroonezero.android.audio_mixer.AudioMixer;
import zeroonezero.android.audio_mixer.input.AudioInput;
import zeroonezero.android.audio_mixer.input.BlankAudioInput;
import zeroonezero.android.audio_mixer.input.GeneralAudioInput;

import java.io.File;
import java.io.IOException;

public class MergeFiles extends AppCompatActivity{
    private Button downloadTrack, shareButton, backToMainPageButton;
    private TextView logoTxt;
    private final String LogTagSharing = "SHARING";
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_files);

        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        File dstFolder = new File(getFilesDir(), "my_records");

        try {
            if (!dstFolder.exists()) {
                if (!dstFolder.mkdirs()) {
                    Log.v("COPY_FILE", "Failed to create folder.");
                    return;
                }
            }
        } catch (Exception e){
            Log.e(LogTagSharing, e.getMessage());
            return;
        }

            downloadTrack = findViewById(R.id.downloadTrack);
            shareButton = findViewById(R.id.shareButton);
            backToMainPageButton = findViewById(R.id.backToMainPageButton);
            logoTxt = findViewById(R.id.logoTxt);

            dialog = new SpotsDialog(this,"");
            dialog.show();

            int musicLength = TransmissionInformation.getInstance().getSongTime();
            Log.e("endTime !!!!!!!!!!!!!!", String.valueOf(musicLength));
            File recordedVoice = TransmissionInformation.getInstance().getFile();
            String recordVoicePath = recordedVoice.toURI().toString();
            Uri musicUri = TransmissionInformation.getInstance().getUri();
            Uri voiceRecordUri = Uri.parse(recordVoicePath);
            String finalSongName = TransmissionInformation.getInstance().getSongName();
            float songVolume = TransmissionInformation.getInstance().getVolumeSong();
            float voiceVolume = TransmissionInformation.getInstance().getVolumeVoice();

            AudioInput input1;
            AudioInput input2;

            try {

                input1 = new GeneralAudioInput(MergeFiles.this, musicUri, null);
                input2 = new GeneralAudioInput(MergeFiles.this, voiceRecordUri, null);
            } catch (IOException exception) {
                Toast.makeText(this, "Bad inputs", Toast.LENGTH_SHORT).show();
                return;
            }

            input1.setVolume(songVolume);
            input2.setVolume(voiceVolume);//Optional
            // It will produce a blank portion of 3 seconds between input1 and input2 if mixing type is sequential.
            // But it will does nothing in parallel mixing.
            //AudioInput blankInput = new BlankAudioInput(3000000); //

            input1.setStartTimeUs(100000); //Optional
            input1.setEndTimeUs(musicLength); //Optional
            ((GeneralAudioInput) input2).setStartOffsetUs(1);
            ((GeneralAudioInput) input1).setStartOffsetUs(100000);//Optional. It is needed to start mixing the input at a certain time.
            String outputPath = dstFolder.getPath() + "/" + finalSongName + ".mp3"; // for example(MY NAME)


            AudioMixer audioMixer;

            File finalTrack = new File(outputPath);

            try {
                audioMixer = new AudioMixer(outputPath);
                audioMixer.addDataSource(input1);
                //audioMixer.addDataSource(blankInput);
                audioMixer.addDataSource(input2);
            } catch (IOException exception) {
                Toast.makeText(this, "Bad output", Toast.LENGTH_SHORT).show();
                return;
            }


            audioMixer.setSampleRate(44100); // Optional
            audioMixer.setBitRate(128000); // Optional
            audioMixer.setChannelCount(2); // Optional //1(mono) or 2(stereo)

            // Smaller audio inputs will be encoded from start-time again if it reaches end-time
            // It is only valid for parallel mixing
            audioMixer.setLoopingEnabled(false);
            audioMixer.setMixingType(AudioMixer.MixingType.PARALLEL); // or AudioMixer.MixingType.SEQUENTIAL
            audioMixer.setProcessingListener(new AudioMixer.ProcessingListener() {
                @Override
                public void onProgress(final double progress) {
                }

                @Override
                public void onEnd() {
                    runOnUiThread(() -> {
                        Toast.makeText(MergeFiles.this, "Success!!!", Toast.LENGTH_SHORT).show();
                        audioMixer.release();
                        dialog.dismiss();
                    });
                }
            });

            try {
                audioMixer.start();
            } catch (IOException exception) {
                Toast.makeText(this, "Bad start", Toast.LENGTH_SHORT).show();
                return;
            }

            //it is for setting up the all the things


            /* These getter methods must be called after calling 'start()'*/
            //audioMixer.getOutputSampleRate();
            //audioMixer.getOutputBitRate();
            //audioMixer.getOutputChannelCount();
            //audioMixer.getOutputDurationUs();

            //starting real processing
            audioMixer.processAsync();

            // We can stop the processing immediately by calling audioMixer.stop() when we want.

            // audioMixer.processSync() is generally not used.
            // We have to use this carefully.
            // Tt will do the processing in caller thread
            // And calling audioMixer.stop() from the same thread won't stop the processing

            logoTxt.setOnClickListener(v -> {
                Intent intent = new Intent(MergeFiles.this, MainActivity.class);
                startActivity(intent);
            });

            backToMainPageButton.setOnClickListener(v -> {
                Intent intent = new Intent(MergeFiles.this, MainActivity.class);
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
                File recordedSong = new File(outputPath);
                Uri resUri = FileProvider.getUriForFile(this, "com.example.musicapp", recordedSong);
                DownloadManager.Request request = new DownloadManager.Request(resUri);
                request.setTitle(finalSongName);
                request.setDescription("Downloading");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, finalSongName);

                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);

                Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show();
            });
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

