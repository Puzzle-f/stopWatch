package com.example.myapplication;

import android.media.MediaPlayer;
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
    private long lastPause;
    private ImageButton pauseButton;
    private final int WINTER_TIME = 7000; //2700000 45 минут
    private ImageButton buttonWinter;
    private ImageButton camp;
    private boolean buttonWinterIsChecked = false;
    private boolean onPause = true;
    private boolean buttonCampIsChecked = false;
    private boolean signalIsPlayCamp = false;
    private boolean signalIsPlayWinter = false;
    private boolean alarmWinterIsChecked = false;
    private boolean alarmCampIsChecked = false;
    private final String TIME_MILLIS = "time";
    private final int CAMP_PTM = 3000; //5*1000*60 5 минут
    private TextView textViewPTM;
    private TextView textViewOverstatement;
    long elapsedMillis;


    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        long elapsedTime = savedInstanceState.getLong(TIME_MILLIS);
        onPause = savedInstanceState.getBoolean("onPause");
        if (!onPause) {
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
        outState.putLong(TIME_MILLIS, SystemClock.elapsedRealtime() - chronometer.getBase());
        outState.putBoolean("onPause", onPause);
        outState.putBoolean("PTM", buttonCampIsChecked);
        Log.d(TAG, "onSaveInstanceState " + (SystemClock.elapsedRealtime() - chronometer.getBase()));
    }

    public void buttonStart(View view) {
        System.out.println("chronometer.getX() " + chronometer.getContentDescription());
        start();
        signalIsPlayCamp = false;
        signalIsPlayWinter = false;
        buttonCampIsChecked = false;
        Log.i(TAG, "start");
    }

    public void start() {
        long systemCurrTime = SystemClock.elapsedRealtime();
        chronometer.setBase(systemCurrTime - 1);
        chronometer.start();
        onPause = false;
        signalIsPlayCamp = false;
        signalIsPlayWinter = false;
//        pauseButton.setEnabled(true);
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
//        listener();
        Log.i(TAG, "onCreate");
    }


    public void buttonPause(View view) {
        if (!onPause) {
            lastPause = SystemClock.elapsedRealtime();
            chronometer.stop();
            onPause = true;
            Log.i(TAG, "pause " + onPause);
        } else {
            chronometer.setBase(chronometer.getBase() + SystemClock.elapsedRealtime() - lastPause);
            chronometer.start();
            onPause = false;
            signalIsPlayCamp = false;
            signalIsPlayWinter = false;
            Log.i(TAG, "pause " + onPause);
        }
    }

    public void buttonStop(View view) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.stop();
        pauseButton.setEnabled(false);
        onPause = true;
        signalIsPlayCamp = false;
        signalIsPlayWinter = false;
        buttonCampIsChecked = false;
        visualization();
        Log.i(TAG, "stop");

    }


    public void signal() {
        MediaPlayer mp;
        mp = MediaPlayer.create(this, R.raw.sound);
        mp.setLooping(false);
        mp.start();
        Log.d(TAG, "signal " + signalIsPlayCamp);
    }

    protected void onResume() {
        super.onResume();
        if (!onPause) {
            chronometer.start();
        }
        Log.d(TAG, "onResume ");
    }

    public void visualization() {
//        Winter
        if (!buttonWinterIsChecked) buttonWinter.setImageResource(R.drawable.check_box1);
        if ((buttonWinterIsChecked) && (alarmWinterIsChecked))
            buttonWinter.setImageResource(R.drawable.check_box3);
        else if (buttonWinterIsChecked) buttonWinter.setImageResource(R.drawable.check_box2);
        if (!buttonCampIsChecked) camp.setImageResource(R.drawable.kp);
        if ((buttonCampIsChecked) && (alarmCampIsChecked)) camp.setImageResource(R.drawable.kp3);
        else if (buttonCampIsChecked) camp.setImageResource(R.drawable.kp2);
        if (!buttonCampIsChecked) alarmCampIsChecked = false;
//          Camp
        if (!buttonCampIsChecked) {
            camp.setImageResource(R.drawable.kp);
            textViewPTM.setVisibility(View.INVISIBLE);
            textViewOverstatement.setVisibility(View.INVISIBLE);
        }
        if ((buttonCampIsChecked) && (alarmCampIsChecked)) {
            camp.setImageResource(R.drawable.kp3);
            textViewPTM.setVisibility(View.VISIBLE);
            textViewOverstatement.setVisibility(View.VISIBLE);
        } else if (buttonCampIsChecked) {
            camp.setImageResource(R.drawable.kp2);
            textViewPTM.setVisibility(View.INVISIBLE);
            textViewOverstatement.setVisibility(View.INVISIBLE);
        }
        if (!buttonWinterIsChecked) alarmWinterIsChecked = false;
//        Log.i(TAG, "visualization");
    }

    public void buttonWinter(View view) {
        if (buttonWinterIsChecked) {
            buttonWinterIsChecked = false;
            signalIsPlayWinter = false;
            visualization();
            Log.d(TAG, "winter " + buttonWinterIsChecked);
        } else {
            buttonWinterIsChecked = true;
            visualization();
            Log.d(TAG, "winter " + buttonWinterIsChecked);
            chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                    if ((elapsedMillis >= WINTER_TIME) && (buttonWinterIsChecked) && (!signalIsPlayWinter)) {
                        signalIsPlayWinter = true;
                        alarmWinterIsChecked = true;
                        signal();
                    } else if ((elapsedMillis >= WINTER_TIME) && (buttonWinterIsChecked) && (signalIsPlayWinter)) {
                        alarmWinterIsChecked = true;
                    }
                    if ((elapsedMillis < WINTER_TIME) && (buttonWinterIsChecked)) {
                        signalIsPlayWinter = false;
                        alarmWinterIsChecked = false;
                    } else if (!buttonWinterIsChecked) {
                        signalIsPlayWinter = false;
                        alarmWinterIsChecked = false;
                    }
                    visualization();
                }
            });
        }
    }

    public void buttonCamp(View view) {
        if (buttonCampIsChecked) {
            buttonCampIsChecked = false;
            signalIsPlayCamp = false;
            visualization();
            Log.d(TAG, "buttonCamp " + buttonCampIsChecked);
        } else {
            buttonCampIsChecked = true;
            onPause = false;
            pauseButton.setEnabled(true);
            visualization();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            Log.d(TAG, "buttonCamp " + buttonCampIsChecked);
                chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                        if ((elapsedMillis >= CAMP_PTM) && (buttonCampIsChecked) && (!signalIsPlayCamp)) {
                            signalIsPlayCamp = true;
                            alarmCampIsChecked = true;
                            signal();
                        } else if ((elapsedMillis >= CAMP_PTM) && (buttonCampIsChecked) && (signalIsPlayCamp)) alarmCampIsChecked = true;
                        if ((elapsedMillis < CAMP_PTM) && (buttonCampIsChecked)) {
                            alarmCampIsChecked = false;
                            signalIsPlayCamp = false;
                        } else if (!buttonCampIsChecked) {
                            signalIsPlayCamp = false;
                            alarmCampIsChecked = false;
                        }
                        visualization();
                    }
                });
        }
    }
}