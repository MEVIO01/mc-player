package com.example.m_player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button btnPlay, btnNext, btnPrev, btnFf, btnFr;
    TextView txtsName, txtsStart, txtsStop;
    SeekBar seekMusic;
    BarVisualizer visualizer;

    String sname;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btnPrev = findViewById(R.id.btnprev);
        btnNext = findViewById(R.id.btnnext);
        btnPlay = findViewById(R.id.playbtn);
        btnFf = findViewById(R.id.btnff);
        btnFr = findViewById(R.id.btnfr);
        txtsName = findViewById(R.id.txtsn);
        txtsStart = findViewById(R.id.txtsstart);
        txtsStop = findViewById(R.id.txtsstop);
        seekMusic = findViewById(R.id.seekbar);
        visualizer = findViewById(R.id.blast);

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        songs = (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songname");
        position = bundle.getInt("pos", 0);
        txtsName.setSelected(true);
        Uri uri = Uri.parse(songs.get(position).toString());
        sname = songs.get(position).getName();
        txtsName.setText(sname);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    btnPlay.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }
                else{
                    btnPlay.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }
            }
        });
    }
}