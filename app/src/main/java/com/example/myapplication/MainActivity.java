package com.example.myapplication;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

private Chronometer chronometer;
private static final String TAG = MainActivity.class.getSimpleName();
private boolean onPause = true;
private long lastPause;
private MediaPlayer mp;
private Chronometer time;
private ImageButton pauseButton;
private final int WINTER_TIME = 5000; // 45 минут 2700000
private ImageButton buttonWinter;
private ImageButton camp;
private boolean buttonWinterIsChecked = false;
private final String TIME_MILLIS = "time";
private boolean buttonCampIsCgecked = false;
private final int CAMP_PTM = 5*1000*60; // 5 минут
private TextView textViewPTM;
private TextView textViewOverstatement;
private boolean signalIsPlay = false;


    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        long elapsedTime = savedInstanceState.getLong(TIME_MILLIS);
        onPause = savedInstanceState.getBoolean("onPause");
        if(!onPause){
            chronometer.setBase(SystemClock.elapsedRealtime() - elapsedTime);
        } else {
            chronometer.stop();
//            chronometer.setBase( SystemClock.elapsedRealtime() - elapsedTime);
        }
        Log.d(TAG, "onRestoreInstanceState " + elapsedTime);
    }

        @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TIME_MILLIS, SystemClock.elapsedRealtime()- chronometer.getBase());
        outState.putBoolean("onPause", onPause);
        outState.putBoolean("PTM", buttonCampIsCgecked);
        Log.d(TAG, "onSaveInstanceState " + (SystemClock.elapsedRealtime()- chronometer.getBase()));
    }

    public void buttonStart(View view) {
        System.out.println("chronometer.getX() " + chronometer.getContentDescription());
        start();
        signalIsPlay = false;
        Log.i(TAG, "start");
    }

    public void start (){
        long systemCurrTime = SystemClock.elapsedRealtime();
        chronometer.setBase(systemCurrTime-1);
        chronometer.start();
        onPause = false;
        buttonCampIsCgecked = false;
        signalIsPlay = false;
        pauseButton.setEnabled(true);
        camp.setImageResource(R.drawable.kp);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chronometer = findViewById(R.id.chronometer);
        this.pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        this.buttonWinter = (ImageButton) findViewById(R.id.winter);
        this.camp = (ImageButton) findViewById(R.id.camp);
        this.textViewPTM = (TextView) findViewById(R.id.textViewPTM);
        this.textViewOverstatement = (TextView) findViewById(R.id.textViewOverstatement);
        pauseButton.setEnabled(false);
        getSupportActionBar().hide(); // убрать шапку приложения
        Log.i(TAG, "onCreate");
    }

    public void buttonPause(View view) {
        if(!onPause){
            lastPause = SystemClock.elapsedRealtime();
            chronometer.stop();
            onPause = true;
            Log.i(TAG, "pause1");
        } else {
            chronometer.setBase(chronometer.getBase() + SystemClock.elapsedRealtime() - lastPause);
            chronometer.start();
            Log.i(TAG, "pause2");
            onPause = false;
            signalIsPlay = false;
        }
        System.out.println("lastPause " + lastPause);
    }

    public void buttonStop(View view) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.stop();
        pauseButton.setEnabled(false);
        onPause = true;
        Log.i(TAG, "stop");
        signalIsPlay = false;
    }


    public void buttonWinter(View view) {
        if(buttonWinterIsChecked){
            Log.d(TAG, "winter "+ buttonWinterIsChecked);
            buttonWinter.setImageResource(R.drawable.check_box1);
            buttonWinterIsChecked = false;
            signalIsPlay = false;
        } else {
            Log.d(TAG, "winter "+ buttonWinterIsChecked);
            buttonWinter.setImageResource(R.drawable.check_box2);
            chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                    if( (elapsedMillis >= WINTER_TIME)&&(buttonWinterIsChecked) ){
                        signal();
                        buttonWinter.setImageResource(R.drawable.check_box3);
                    } else buttonWinter.setImageResource(R.drawable.check_box2);
                }
            });
            buttonWinterIsChecked = true;
        }
    }

    public void signal(){
        if (!signalIsPlay){
            mp = MediaPlayer.create(this, R.raw.sound);
            mp.setLooping(false);
            mp.start();
        }
        signalIsPlay = true;
    }

    protected void onResume() {
        super.onResume();
        if(!onPause){
            chronometer.start();
        }
        Log.d(TAG, "onResume ");
    }


    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }
    protected void onDestroy() {
        super.onDestroy();
//        chronometer.setBase( SystemClock.elapsedRealtime());
        Log.d(TAG, "onDestroy");
    }

    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    public void buttonCamp(View view) {
        if(!buttonCampIsCgecked){
            Log.d(TAG, "camp");
            buttonCampIsCgecked = true;
            camp.setImageResource(R.drawable.kp2);
            long systemCurrTime = SystemClock.elapsedRealtime();
            chronometer.setBase(systemCurrTime-1);
            chronometer.start();
            onPause = false;
            pauseButton.setEnabled(true);

            chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    if(buttonCampIsCgecked){
                        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                        if( (elapsedMillis >= CAMP_PTM)&&((elapsedMillis - CAMP_PTM) <= (1000)) ){
                            signal();
                            textViewOverstatement.setVisibility(View.VISIBLE);
                            textViewPTM.setVisibility(View.VISIBLE);
                            camp.setImageResource(R.drawable.kp3);
                        }
                    }
                }
            });

    } else {
        buttonCampIsCgecked = false;
        textViewOverstatement.setVisibility(View.INVISIBLE);
        textViewPTM.setVisibility(View.INVISIBLE);
        camp.setImageResource(R.drawable.kp);
    }

    }
}