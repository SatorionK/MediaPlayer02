package com.example.mediaplayer02;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //browser、controller
    MediaBrowserCompat mBrowser;
    MediaControllerCompat mController;

    //UI画面上のウィジェット
    ImageView imageView;
    TextView artist_name;
    TextView music_name;
    TextView time_position;
    TextView time_duration;
    ImageButton back;
    ImageButton next;
    ImageButton repeat;
    ImageButton shuff;
    FloatingActionButton play;
    SeekBar seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        artist_name = findViewById(R.id.textView_artistname);
        music_name = findViewById(R.id.textView_musicname);
        time_position = findViewById(R.id.textView_position);
        time_duration = findViewById(R.id.textView_duration);

        back = findViewById(R.id.button_back);
        next = findViewById(R.id.button_next);
        repeat = findViewById(R.id.button_repeat);
        shuff = findViewById(R.id.button_shuff);
        play = findViewById(R.id.button_play);
        seekbar = findViewById(R.id.seekBar);


        //戻るボタンの実装
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.getTransportControls().skipToPrevious();
            }
        });

        //戻るボタンの実装
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.getTransportControls().skipToNext();
            }
        });

        //seekbarの実装
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mController.getTransportControls().seekTo(seekBar.getProgress());
            }
        });

        //サービスは開始
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        startService(intent);

        //browserのインスタンス化
        mBrowser = new MediaBrowserCompat(this, new ConpornetName(this, MusicService.class), connectionCallback, null);
        mBrowser.connect();

    }
//--------------------------------------------------------------------------------------------------



}
