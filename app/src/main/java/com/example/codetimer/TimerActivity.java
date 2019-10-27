package com.example.codetimer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
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
    private long mTimeLeft;
    private int currentPos;
    private int mRepsLeft = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Toolbar mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        elements = (ArrayList<ListElement>) intent.getSerializableExtra(MainActivity.EXTRAMESSAGE);
        currentPos = getNextPos(-1);

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

    }

    private void startTimer() {
        name_view.setText(elements.get(currentPos).getName());
        mTimeLeft = elements.get(currentPos).getRepetition();
        mCountDownTimer = new CountDownTimer(mTimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeft = millisUntilFinished;
                updateTimerText(mTimeLeft);
            }

            @Override
            public void onFinish() {
                currentPos = getNextPos(currentPos);
                Log.i("TIMER CURRENTPOS",""+currentPos);
                if (currentPos == -1){
                    currentPos = getNextPos(currentPos);
                    TimerRunning = false;
                    return;
                }
                startTimer();
            }
        }.start();

        TimerRunning = true;
    }

    private int getNextPos(int pos){
        pos++;
        ListElement el = elements.get(pos);
        if (pos > elements.size()-2){
            return -1;
        }

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

    }
}
