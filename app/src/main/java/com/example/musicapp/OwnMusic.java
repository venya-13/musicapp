package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class OwnMusic extends AppCompatActivity {
    private ListView listViewSong;
    private SearchView searchOwnMusic;

    ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());

    String[] items = new String[mySongs.size()];

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_music);

        listViewSong = findViewById(R.id.listViewSong);
        searchOwnMusic = findViewById(R.id.searchOwnMusic);
        searchOwnMusic.clearFocus();
        searchOwnMusic.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });

        displaySongs(mySongs);

    }

    private void filterList(String text) {
        ArrayList<File> filteredList = new ArrayList<>();

        for (File file : mySongs){
            if (file.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(file);
            }
        }

        if (filteredList.isEmpty()){
            setFilteredList(mySongs);
        } else{
            setFilteredList(filteredList);
        }
    }

    public void setFilteredList(ArrayList<File> filteredList){
        displaySongs(filteredList);
    }

    public ArrayList<File> findSong(File file){

        ArrayList<File> songList = new ArrayList<>();

        File[] files = file.listFiles();

        for(File singleFile: files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                songList.addAll(findSong(singleFile));
            }
            else {
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    songList.add(singleFile);
                }
            }
        }
        return songList;
    }

    void displaySongs(ArrayList<File> songs){
        for (int i = 0; i < songs.size(); i++){
            items[i] = songs.get(i).getName().toString();
        }

        CustomAdapter customAdapter = new CustomAdapter();
        listViewSong.setAdapter(customAdapter);



        listViewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String musicPath = (songs.get(position)).getPath();
                Log.d("Register class", "onFailure: "+ musicPath);
                Uri uri = Uri.parse(songs.get(position).toString());

                TransmissionInformation.getInstance().setUri(uri);
                TransmissionInformation.getInstance().setString2(musicPath);

                String songName = (String) listViewSong.getItemAtPosition(position);
                startActivity(new Intent(getApplicationContext(), CheckSongActivity.class)
                .putExtra("songs", songs)
                .putExtra("song name", songName)
                .putExtra("position", position));

            }
        });
    }



    class CustomAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            progressDialog.dismiss();
            View myView = getLayoutInflater().inflate(R.layout.music_list_background, null);
            TextView songName = myView.findViewById(R.id.songName);
            songName.setSelected(true);
            songName.setText(items[i]);
            notifyDataSetChanged();

            return myView;
        }
    }



}