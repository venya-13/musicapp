package com.example.musicapp;

import android.net.Uri;

public class UploadUri {
    private String mName;
    private Uri mUri;

    public UploadUri(){

    }

    public UploadUri(String name, Uri uri){
        if (name.trim().equals("")){
            name = "No Name";
        }

        mName = name;
        mUri = uri;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public Uri setUri(){
        return mUri;
    }

    public void setUri(Uri uri){
        mUri = uri;
    }
}
