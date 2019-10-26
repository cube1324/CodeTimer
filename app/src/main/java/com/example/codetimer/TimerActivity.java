package com.example.codetimer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.codetimer.Support.ListElement;

import java.util.ArrayList;
import java.util.LinkedList;

public class TimerActivity extends AppCompatActivity {
    private ArrayList<ListElement> elements;
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

        


    }
}
