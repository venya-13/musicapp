package com.example.musicapp;

import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class transmissionInformation extends MainActivity {
    private String Something;
    private int Number;
    private Uri Uri;
    private File File;

    private transmissionInformation(){

    }

    private static transmissionInformation instance;

    public static transmissionInformation getInstance(){
        if(instance == null){
            instance = new transmissionInformation();
        }
        return instance;
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