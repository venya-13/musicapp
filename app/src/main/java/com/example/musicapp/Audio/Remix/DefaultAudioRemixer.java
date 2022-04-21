package com.example.musicapp.Audio.Remix;

import androidx.annotation.NonNull;

import java.nio.ShortBuffer;

public class DefaultAudioRemixer implements AudioRemixer {

    @Override
    public void remix(@NonNull ShortBuffer inputBuffer, int inputChannelCount, @NonNull ShortBuffer outputBuffer, int outputChannelCount) {
        AudioRemixer remixer;
        if (inputChannelCount > outputChannelCount) {
            remixer = DOWNMIX;
        } else if (inputChannelCount < outputChannelCount) {
            remixer = AudioRemixer.UPMIX;
        } else {
            remixer = AudioRemixer.PASSTHROUGH;
        }
        remixer.remix(inputBuffer, inputChannelCount, outputBuffer, outputChannelCount);
    }

    @Override
    public int getRemixedSize(int inputSize, int inputChannelCount, int outputChannelCount) {
        AudioRemixer remixer;
        if (inputChannelCount > outputChannelCount) {
            remixer = DOWNMIX;
        } else if (inputChannelCount < outputChannelCount) {
            remixer = AudioRemixer.UPMIX;
        } else {
            remixer = AudioRemixer.PASSTHROUGH;
        }
        return remixer.getRemixedSize(inputSize, inputChannelCount, outputChannelCount);
    }
}
