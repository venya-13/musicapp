package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import zeroonezero.android.audio_mixer.AudioMixer;
import zeroonezero.android.audio_mixer.input.AudioInput;
import zeroonezero.android.audio_mixer.input.BlankAudioInput;
import zeroonezero.android.audio_mixer.input.GeneralAudioInput;

import java.io.File;
import java.io.IOException;

public class MergeFiles extends AppCompatActivity{
    private Button downloadTrack, shareButton, backToMainPageButton;
    private TextView logoTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_files);

        downloadTrack = findViewById(R.id.downloadTrack);
        shareButton = findViewById(R.id.shareButton);
        backToMainPageButton = findViewById(R.id.backToMainPageButton);
        logoTxt = findViewById(R.id.logoTxt);

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MergeFiles.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

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

            input1 = new GeneralAudioInput(MergeFiles.this, musicUri,null);
            input2 = new GeneralAudioInput(MergeFiles.this, voiceRecordUri,null);
        }catch (IOException exception){
            Toast.makeText(this, "Bad inputs", Toast.LENGTH_SHORT).show();
            return;
        }

        input1.setVolume(0.5f);
        input2.setVolume(0.7f);//Optional
        // It will produce a blank portion of 3 seconds between input1 and input2 if mixing type is sequential.
        // But it will does nothing in parallel mixing.
        //AudioInput blankInput = new BlankAudioInput(3000000); //

        input1.setStartTimeUs(100000); //Optional
        input1.setEndTimeUs(musicLength); //Optional
        ((GeneralAudioInput) input2).setStartOffsetUs(1); //Optional. It is needed to start mixing the input at a certain time.
        String outputPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/" +finalSongName +".mp3"; // for example(MY NAME)

        Log.d("String", "!!!!!!!!!!!!!!!!"+ outputPath);

        AudioMixer audioMixer;

        File finalTrack = new File(outputPath);

        try {
            audioMixer = new AudioMixer(outputPath);
            audioMixer.addDataSource(input1);
            //audioMixer.addDataSource(blankInput);
            audioMixer.addDataSource(input2);
        } catch (IOException exception){
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
                runOnUiThread(() -> progressDialog.setProgress((int) (progress * 100)));
            }
            @Override
            public void onEnd() {
                runOnUiThread(() -> {
                    Toast.makeText(MergeFiles.this, "Success!!!", Toast.LENGTH_SHORT).show();
                    audioMixer.release();
                    progressDialog.dismiss();
                });
            }
        });

        try {
            audioMixer.start();
        } catch (IOException exception){
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
            shareDialog(outputPath,finalSongName);
        });

        downloadTrack.setOnClickListener(v -> {
            Uri uri = Uri.parse(outputPath);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(finalSongName);
            request.setDescription("Downloading");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,finalSongName);

            DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);

            Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show();
        });

    }

    private void shareDialog(String path,String finalSongName){
////        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
////                +"/" +"0035" +".wav");
//
////        final Uri[] uri = new Uri[1];
//
////        Uri uri = FileProvider.getUriForFile(getApplicationContext(),getPackageName() + ".fileProvider", file);
//
//        //Uri uri = Uri.fromFile(file);
//        Uri uri = Uri.parse(path);
//
////        MediaScannerConnection.scanFile(this, new String[] { file.getAbsolutePath() }, null,
////                (path1, uri1) -> uri[0] = uri1);
//
        Uri uri = Uri.parse(path);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("audio/*");
        sendIntent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Intent shareIntent = Intent.createChooser(sendIntent, "name");
        startActivity(shareIntent);

//        String[] projection = new String[]{
//                MediaStore.Video.Media._ID,
//                MediaStore.Video.Media.DISPLAY_NAME
//        };
//
//        String selection = MediaStore.Video.Media.DISPLAY_NAME + " == ?";
//
//        String[] selectionArgs = new String[] {
//                finalSongName + ".mp3"
//        };
//
//        Cursor cursor = getContentResolver().query(
//                songUri,
//                projection,
//                selection,
//                selectionArgs,
//                null
//        );
//
//        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
//        int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
//
//
//        if (! cursor.moveToNext() ) {
//            return;
//        }
//
//        long id = cursor.getInt(idColumn);
//        String name = cursor.getString(nameColumn);
//
//        Uri contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
//
//
//        try {
//            Intent shareIntent = new Intent(Intent.ACTION_SEND); // create action send
//            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//            // create mime type of file from file uri
//            String mimeType;
//            if (ContentResolver.SCHEME_CONTENT.equals(contentUri.getScheme())) {
//                ContentResolver cr = getContentResolver();
//                mimeType = cr.getType(contentUri);
//            } else {
//                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(contentUri.toString());
//                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
//                        fileExtension.toLowerCase());
//            }
//
//            // shareIntent.setPackage(""); // you can specify app package name here to share file that app only
//            shareIntent.setDataAndType(contentUri, mimeType);
//            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
//            startActivity(Intent.createChooser(shareIntent, "Share File"));
//        } catch (Throwable throwable) {
//            Log.i("SHARE", "shareSong: error :- " + throwable.getMessage());
//        }


    };
}

