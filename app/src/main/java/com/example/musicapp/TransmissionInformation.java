package com.example.musicapp;

import android.net.Uri;

import java.io.File;

public class TransmissionInformation extends MainActivity {
    private String Something,SongName,RecordPath, AlreadyRecordedTrackPath,AlreadyRecordedTrackName;
    private int Number,SongTime, AlreadyRecordedTrackPosition;
    private Uri Uri;
    private Uri UriOfAlreadyRecordedSong;
    private File File;
    private float voiceVolume;
    private float songVolume;
    private String[] items;


    private TransmissionInformation(){

    }

    private static TransmissionInformation instance;

    public static TransmissionInformation getInstance(){
        if(instance == null){
            instance = new TransmissionInformation();
        }
        return instance;
    }

    public void setSongName (String param){
        SongName = param;

    }

    public String getSongName (){

        return SongName;
    }

    public void setString (String param){
        RecordPath = param;

    }

    public String getString (){

        return RecordPath;
    }

    public void setAlreadyRecordedTrackPath (String param){
        AlreadyRecordedTrackPath = param;

    }

    public String getAlreadyRecordedTrackPath (){

        return AlreadyRecordedTrackPath;
    }

    public void setAlreadyRecordedTrackName (String param){
        AlreadyRecordedTrackName = param;

    }

    public String getAlreadyRecordedTrackName (){

        return AlreadyRecordedTrackName;
    }

    public void setString2 (String param){
        Something = param;

    }

    public String getString2 (){

        return Something;
    }

    public void setNumber (int param){
        Number = param;

    }

    public int getNumber (){

        return Number;
    }

    public void setAlreadyRecordedTrackPosition (int param){
        AlreadyRecordedTrackPosition = param;

    }

    public int getAlreadyRecordedTrackPosition (){

        return AlreadyRecordedTrackPosition;
    }

    public void setVolumeVoice (float param){
        voiceVolume = param;

    }

    public float getVolumeVoice (){

        return voiceVolume;
    }

    public void setVolumeSong (float param){
        songVolume = param;

    }

    public float getVolumeSong (){

        return songVolume;
    }

    public void setUri (Uri param){
        Uri = param;

    }

    public Uri getUri (){

        return Uri;
    }

    public void setAlreadyRecordedUri (Uri param){
        UriOfAlreadyRecordedSong = param;

    }

    public Uri getAlreadyRecordedUri (){

        return UriOfAlreadyRecordedSong;
    }

    public void setFile (File param){
        File = param;

    }

    public File getFile (){

        return File;
    }

    public void setSongTime (int param){
        SongTime = param;

    }

    public int getSongTime (){

        return SongTime;
    }

    public void setItems (String[] param){
        items = param;

    }

    public String[] getItems (){

        return items;
    }
}