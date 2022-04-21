package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.github.piasy.audio_mixer.AudioMixer;

import java.io.File;

public class MergeFiles extends AppCompatActivity implements {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_files);

        Uri uri = transmissionInformation.getInstance().getUri();
        File recordedVoice = transmissionInformation.getInstance().getFile();

        AudioInput input1 = new GeneralAudioInput(uri);
        input1.setVolume(0.5f); //Optional
        // It will produce a blank portion of 3 seconds between input1 and input2 if mixing type is sequential.
        // But it will does nothing in parallel mixing.
        AudioInput blankInput = new BlankAudioInput(3000000); //
        AudioInput input2 = new GeneralAudioInput(recordedVoice);
        input2.setStartTimeUs(3000000); //Optional
        input2.setEndTimeUs(9000000); //Optional
        input2.setStartOffsetUs(5000000); //Optional. It is needed to start mixing the input at a certain time.
        String outputPath = Environment.getDownloadCacheDirectory().getAbsolutePath()
                +"/" +"audio_mixer_output.mp3"; // for example
        final AudioMixer audioMixer = new AudioMixer(outputPath);
        audioMixer.addDataSource(input1);
        audioMixer.addDataSource(blankInput);
        audioMixer.addDataSource(input2);
        audioMixer.setSampleRate(44100); // Optional
        audioMixer.setBitRate(128000); // Optional
        audioMixer.setChannelCount(2); // Optional //1(mono) or 2(stereo)

        // Smaller audio inputs will be encoded from start-time again if it reaches end-time
        // It is only valid for parallel mixing
        //audioMixer.setLoopingEnabled(true);
        audioMixer.setMixingType(AudioMixer.MixingType.PARALLEL); // or AudioMixer.MixingType.SEQUENTIAL
        audioMixer.setProcessingListener(new AudioMixer.ProcessingListener() {
            @Override
            public void onProgress(final double progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setProgress((int) (progress * 100));
                    }
                });
            }
            @Override
            public void onEnd() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Success!!!", Toast.LENGTH_SHORT).show();
                        audioMixer.release();
                    }
                });
            }
        });


        //it is for setting up the all the things
        audioMixer.start();

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

    }
}