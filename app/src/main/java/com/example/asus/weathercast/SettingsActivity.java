package com.example.asus.weathercast;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity  {


    private GestureDetectorCompat gestureDetectorCompat = null;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        LinearLayout settingsview ;
        settingsview =findViewById(R.id.settings);




        DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener();


        gestureListener.setActivity(this);

        // Create the gesture detector with the gesture listener.
        gestureDetectorCompat = new GestureDetectorCompat(this, gestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pass activity on touch event to the gesture detector.
        gestureDetectorCompat.onTouchEvent(event);
        // Return true to tell android OS that event has been consumed, do not pass it to other event listeners.
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        DetectSwipeGestureListener.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DetectSwipeGestureListener.activityPaused();
    }
}



