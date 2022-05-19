package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

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

import dmax.dialog.SpotsDialog;

public class AlreadyRecordedTracks extends AppCompatActivity {
    private ListView listViewSong;
    private SearchView searchOwnMusic;
    private SpotsDialog dialog;

    private ArrayList<File> mySongs = new ArrayList<>();
    private String[] items = new String[mySongs.size()];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_music);

        dialog = new SpotsDialog(this,"");
        dialog.show();

        Thread thread = new Thread(() -> {
            createFilesList(mySongs,items);
        });
        thread.start();

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
                filterList(s,mySongs,items);
                return true;
            }
        });
    }

    private void createFilesList (ArrayList<File> songs,String[] items2){
        File dstFolder = new File(getFilesDir(), "my_records");
        String path = dstFolder.getPath().toString();
        try {
            songs = findSong(new File(path));
            items2 = new String[songs.size()];
        }catch (Exception e){
            Log.e("Error!!!!!!!!!!", e.getMessage());
        }
        ArrayList<File> finalMySongs = songs;
        String[] finalItems = items2;
        runOnUiThread(() -> {
            displaySongs(finalMySongs, finalItems);
            mySongs = finalMySongs;
            items = finalItems;
            dialog.dismiss();
        });
    }

    private void filterList(String text, ArrayList<File> mySongs,String[] items) {
        ArrayList<File> filteredList = new ArrayList<>();

        for (File file : mySongs){
            if (file.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(file);
            }
        }

        if (filteredList.isEmpty()){
            setFilteredList(mySongs,items);
        } else{
            setFilteredList(filteredList,items);
        }
    }

    public void setFilteredList(ArrayList<File> filteredList,String[]items){
        displaySongs(filteredList,items);
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

    void displaySongs(ArrayList<File> songs,String[] items){
        for (int i = 0; i < songs.size(); i++){
            items[i] = songs.get(i).getName().toString();
        }
        try {
            CustomAdapter customAdapter = new CustomAdapter(items);
            listViewSong.setAdapter(customAdapter);
        }catch (Exception e){
            Log.e("Error 2!!!!!!!!!!", e.getMessage());
        }

        listViewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String musicPath = (songs.get(position)).getPath();
                Log.d("Register class", "onFailure: "+ musicPath);
                Uri uri = Uri.parse(songs.get(position).toString());

                TransmissionInformation.getInstance().setAlreadyRecordedUri(uri);

                String songName = (String) listViewSong.getItemAtPosition(position);

//                TransmissionInformation.getInstance().setAlreadyRecordedTrackPath(musicPath);
//                TransmissionInformation.getInstance().setAlreadyRecordedTrackName(songName);
//                TransmissionInformation.getInstance().setAlreadyRecordedTrackPosition(position);

                startActivity(new Intent(getApplicationContext(), CheckAlreadySong.class)
                        .putExtra("songs", songs)
                        .putExtra("song name", songName)
                        .putExtra("position", position));

            }
        });
    }



    class CustomAdapter extends BaseAdapter {
        private final String[] items;

        public CustomAdapter(String[] sourceItems){
            items = sourceItems;
        }

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
        public View getView(int i, View view, ViewGroup viewGroup){
            View myView = getLayoutInflater().inflate(R.layout.music_list_background, null);
            TextView songName = myView.findViewById(R.id.songName);
            songName.setSelected(true);
            songName.setText(items[i]);
            notifyDataSetChanged();

            return myView;
        }
    }

}