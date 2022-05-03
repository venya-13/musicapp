package com.example.musicapp;

import android.net.Uri;

import java.io.File;

public class TransmissionInformation extends MainActivity {
    private String Something,SongName,RecordPath;
    private int Number,SongTime;
    private Uri Uri;
    private File File;

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

    public void setVolumeVoice (int param){
        Number = param;

    }

    public int getVolumeVoice (){

        return Number;
    }

    public void setVolumeSong (int param){
        Number = param;

    }

    public int getVolumeSong (){

        return Number;
    }

    public void setUri (Uri param){
        Uri = param;

    }

    public Uri getUri (){

        return Uri;
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
}