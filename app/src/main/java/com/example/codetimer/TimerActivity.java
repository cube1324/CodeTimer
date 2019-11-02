package com.example.codetimer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.codetimer.Support.ItemType;
import com.example.codetimer.Support.ListElement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedList;

public class TimerActivity extends AppCompatActivity {
    private ArrayList<ListElement> elements;

    private TextView text_view_countdown;
    private TextView name_view;
    private FloatingActionButton timer_button;
    private CountDownTimer mCountDownTimer;

    private boolean TimerRunning = false;
    private boolean isPaused = false;
    private long mTimeLeft;
    private int currentPos;

    private SoundPool soundPool;
    private int low_beep, high_beep;

    private MediaPlayer high_player;
    private MediaPlayer low_player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        //Setup Top Bar
        Toolbar mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        //Create Back Button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Keep Screen on Permanently
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //Get elements from Mainactivity
        Intent intent = getIntent();
        elements = (ArrayList<ListElement>) intent.getSerializableExtra(MainActivity.EXTRAMESSAGE);
        currentPos = getNextPos(-1);

        //Initialize Elements in view
        text_view_countdown = findViewById(R.id.text_view_countdown);
        name_view = findViewById(R.id.name_view);
        timer_button = findViewById(R.id.timer_button);

        timer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimerRunning){
                    pauseTimer();
                }else{
                    startTimer();
                }
            }
        });

        //Initialize Textviews
        int i = 0;
        while (elements.get(i).getType() != ItemType.TIMER){
            i++;
        }
        text_view_countdown.setText(elements.get(i).getNumber());
        name_view.setText(elements.get(i).getName());

        //Load MediaPlayer
        high_player = MediaPlayer.create(this, R.raw.middle_beep);
        low_player = MediaPlayer.create(this, R.raw.low_beep);
    }

    private void startTimer() {
        TimerRunning = true;

        updateFloatingButton();

        name_view.setText(elements.get(currentPos).getName());
        if (isPaused){
            isPaused = false;
        }else {
            mTimeLeft = elements.get(currentPos).getRepetition();
        }
        mCountDownTimer = new CountDownTimer(mTimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("TIME LEFT", Long.toString(millisUntilFinished));
                if (millisUntilFinished <= 4000 && millisUntilFinished > 1000){
                    low_player.start();
                } else if (millisUntilFinished <= 1000){
                    high_player.start();
                }
                mTimeLeft = millisUntilFinished;
                updateTimerText(mTimeLeft);
            }

            @Override
            public void onFinish() {
                currentPos = getNextPos(currentPos);
                if (currentPos == -1){
                    //Timer Finished
                    //Set Index to start
                    currentPos = getNextPos(currentPos);
                    TimerRunning = false;
                    updateFloatingButton();
                    return;
                }
                startTimer();
            }
        }.start();
    }

    private void updateFloatingButton(){
        if (TimerRunning){
            timer_button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_32px));
        } else{
            timer_button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_arrow_32px));
        }
    }

    private int getNextPos(int pos){
        pos++;
        if (pos > elements.size()-1 ){
            return -1;
        }
        ListElement el = elements.get(pos);

        if (el.getType() == ItemType.LOOPSTART){
            return getNextPos(pos);
        }
        if (el.getType() == ItemType.LOOPEND){
            if (elements.get(el.getRelatedIndex()).getRepetition() > 1){
                elements.get(el.getRelatedIndex()).incNumberBy(-1);
                return getNextPos(el.getRelatedIndex());
            }else {
                elements.get(el.getRelatedIndex()).setNumber(elements.get(el.getRelatedIndex()).getSave_number());
                return getNextPos(pos);
            }
        }
        return pos;
    }

    private void updateTimerText(long time){
        int min = (int) time / 1000 / 60;
        int sec = (int) time / 1000 % 60;
        String temp = String.format("%02d : %02d", min, sec);
        text_view_countdown.setText(temp);
    }

    private void pauseTimer(){
        mCountDownTimer.cancel();
        TimerRunning = false;
        isPaused = true;
        updateFloatingButton();
    }

    public void onStop(){
        super.onStop();
        mCountDownTimer.cancel();
        high_player.release();
        low_player.release();
    }
}
