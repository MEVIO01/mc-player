package com.example.m_player;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String[] songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listViewSong);
        Log.d("MC Player", "Hello is called");
        runtimePermission();
    }
    public void runtimePermission(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
    public ArrayList<File> findSong(File file){
        ArrayList<File> arrList = new ArrayList<>();

        File[] files = file.listFiles();

        for (File singleFile: files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                arrList.addAll(findSong(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                     arrList.add(singleFile);
                }
            }
        }

        return arrList;
    }

    private void displaySongs(){
        Log.d("MC Player", "Hello : " + Environment.getExternalStorageDirectory().toString());
        final ArrayList<File> availableSongs = findSong(Environment.getExternalStorageDirectory());

        songs = new String[availableSongs.size()];

        for (int i = 0; i < availableSongs.size(); i++){
            songs[i] = availableSongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");

        }
        //ArrayAdapter<String> songsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songs);

        //listView.setAdapter(songsAdapter);

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String songName = (String) listView.getItemAtPosition(i);
                    startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                            .putExtra("songs", availableSongs)
                            .putExtra("songname", songName)
                            .putExtra("pos", i));
                    Log.d("MC Player", "This one is without errors");
            }
        });
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return songs.length;
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
            View resView = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView songText = resView.findViewById(R.id.txtsongname);
            songText.setSelected(true);
            songText.setText(songs[i]);
            return resView;
        }
    }
}