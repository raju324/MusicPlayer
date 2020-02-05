package com.raj.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {

    Button  btnnxt,btnprv,btnpause;
    TextView textView;
    SeekBar songseekbar;
    static MediaPlayer mymediaplayer;
    int position;
    String sname;
    ArrayList<File> mySongs;
    Thread updateseekbar;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btnnxt=findViewById(R.id.next);
        btnprv=findViewById(R.id.previous);
        btnpause=findViewById(R.id.pause);
        songseekbar=findViewById(R.id.seekbar);
        textView=findViewById(R.id.textview);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        updateseekbar=new Thread(){

            @Override
            public void run() {
                int totalDuration= mymediaplayer.getDuration();
                int currentPosition =0;
                while (currentPosition<totalDuration)
                {
                    try
                    {
                    sleep(500);
                    currentPosition=mymediaplayer.getCurrentPosition();
                    songseekbar.setProgress(currentPosition);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }


            }
        };

        if (mymediaplayer!=null)
        {
            mymediaplayer.stop();
            mymediaplayer.release();
        }
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");
        sname=mySongs.get(position).getName().toString();
        String songname=i.getStringExtra("songname");
        textView.setText(songname);
        textView.setSelected(true);

        position= bundle.getInt("pos",0);
        Uri u=Uri.parse(mySongs.get(position).toString());
        mymediaplayer=MediaPlayer.create(getApplicationContext(),u);
        mymediaplayer.start();
        songseekbar.setMax(mymediaplayer.getDuration());
        updateseekbar.start();
        songseekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            songseekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC);


        songseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mymediaplayer.seekTo(seekBar.getProgress());
            }
        });
btnpause.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        songseekbar.setMax(mymediaplayer.getDuration());
        if (mymediaplayer.isPlaying())
        {
            btnpause.setBackgroundResource(R.drawable.play);
            mymediaplayer.pause();
        }
        else
        {
            btnpause.setBackgroundResource(R.drawable.pause);
            mymediaplayer.start();
        }
    }
});
btnnxt.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        mymediaplayer.stop();
        mymediaplayer.release();
        position=((position+1)%mySongs.size());
        Uri u=Uri.parse(mySongs.get(position).toString());
        mymediaplayer=MediaPlayer.create(getApplicationContext(),u);
        sname=mySongs.get(position).getName().toString();
        textView.setText(sname);
        mymediaplayer.start();
    }
});
btnprv.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        mymediaplayer.stop();
        mymediaplayer.release();
        position=((position-1)<0)?(mySongs.size()-1):(position-1);
        Uri u=Uri.parse(mySongs.get(position).toString());
        mymediaplayer=MediaPlayer.create(getApplicationContext(),u);
        sname=mySongs.get(position).getName().toString();
        textView.setText(sname);
        mymediaplayer.start();
    }
});

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home);
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
