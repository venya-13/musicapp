package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ownMusic extends AppCompatActivity {
    private ListView listViewSong;
    private SearchView searchOwnMusic;

    final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());

    String[] items = new String[mySongs.size()];

    int searchFilter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_music);


        listViewSong = findViewById(R.id.listViewSong);
        searchOwnMusic = findViewById(R.id.searchOwnMusic);

        displaySongs();

//        searchOwnMusic.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filteredList(newText);
//                return true;
//            }
//        });

    }


//    private void filteredList(String text) {
//        List<File> filteredList = new ArrayList<>();
//
//        for (File item: mySongs){
//            if (item.getName().toLowerCase().contains(text.toLowerCase())){
//                filteredList.add(item);
//            }
//        }
//        addItemsToList(filteredList);
//    }
//
//    private void addItemsToList() {
//        searchFilter += 1;
//
//    }


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

    void displaySongs(){
        for (int i = 0; i < mySongs.size(); i++){
            items[i] = mySongs.get(i).getName().toString();
        }
//        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
//        listViewSong.setAdapter(myAdapter);

        customAdapter customAdapter = new customAdapter();
        listViewSong.setAdapter(customAdapter);

        listViewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(mySongs.get(position).toString());

                String songName = (String) listViewSong.getItemAtPosition(position);
                startActivity(new Intent(getApplicationContext(), CheckSongActivity.class)
                .putExtra("songs", mySongs)
                .putExtra("song name", songName)
                .putExtra("position", position));

            }
        });
    }



    class customAdapter extends BaseAdapter{
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

            View myView = getLayoutInflater().inflate(R.layout.music_list_background, null);
            TextView songName = myView.findViewById(R.id.songName);
            songName.setSelected(true);
            songName.setText(items[i]);

            return myView;
        }
    }



}