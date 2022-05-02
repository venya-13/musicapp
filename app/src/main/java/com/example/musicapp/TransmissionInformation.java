package com.example.musicapp;

import android.net.Uri;

import java.io.File;

public class TransmissionInformation extends MainActivity {
    private String Something;
    private int Number;
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
        Something = param;

    }

    public String getSongName (){

        return Something;
    }

    public void setString (String param){
        Something = param;

    }

    public String getString (){

        return Something;
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
}