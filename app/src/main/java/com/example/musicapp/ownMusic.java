package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ownMusic extends AppCompatActivity {
    private ListView listViewSong;
    private EditText searchOwnMusic;
    static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_music);

        listViewSong = findViewById(R.id.listViewSong);
        searchOwnMusic = findViewById(R.id.searchOwnMusic);

        displaySongs();

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
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

    void displaySongs(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());

        String[] items = new String[mySongs.size()];
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

                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();

            }
        });
    }

    class customAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
            String[] items = new String[mySongs.size()];
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
        public View getView(int a, View view, ViewGroup viewGroup) {
            final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());

            String[] items = new String[mySongs.size()];
            int i = 0;
            for ( i = 0; i < mySongs.size(); i++){
                items[i] = mySongs.get(i).getName().toString();
            }
            View myView = getLayoutInflater().inflate(R.layout.music_list_background, null);
            TextView songName = myView.findViewById(R.id.songName);
            songName.setSelected(true);
            songName.setText(items[i]);

            return myView;
        }
    }
}