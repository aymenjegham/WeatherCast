package com.example.asus.weathercast;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class  DetectSwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

    Context context;

    // Minimal x and y axis swipe distance.
    private static int MIN_SWIPE_DISTANCE_X = 100;
    private static int MIN_SWIPE_DISTANCE_Y = 100;

    // Maximal x and y axis swipe distance.
    private static int MAX_SWIPE_DISTANCE_X = 1000;
    private static int MAX_SWIPE_DISTANCE_Y = 1000;

    // Source activity that display message in text view.
    private SettingsActivity activity = null;

    public SettingsActivity getActivity() {
        return activity;
    }

    public void setActivity(SettingsActivity activity) {
        this.activity = activity;
    }

    private InfoActivity activityInfo = null;

    public InfoActivity getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(InfoActivity activity) {
        this.activityInfo = activity;
    }

    /* This method is invoked when a swipe gesture happened. */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        // Get swipe delta value in x axis.
        float deltaX = e1.getX() - e2.getX();

        // Get swipe delta value in y axis.
        float deltaY = e1.getY() - e2.getY();

        // Get absolute value.
        float deltaXAbs = Math.abs(deltaX);
        float deltaYAbs = Math.abs(deltaY);

        // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
        if((deltaXAbs >= MIN_SWIPE_DISTANCE_X) && (deltaXAbs <= MAX_SWIPE_DISTANCE_X))
        {
            if(deltaX > 0){
                if(isActivityVisible()== true){
                     Intent intent = new Intent(getActivity().getApplicationContext(),WeatherActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent); //ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle()
                    // this.activity.displayMessage("Swipe to left");
                }else {
                    Intent intent = new Intent(getActivityInfo().getApplicationContext(),WeatherActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivityInfo().startActivity(intent);
                 }




            }else
            {
                if(isActivityVisible()== true) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), WeatherActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                    //this.activity.displayMessage("Swipe to right");
                }else {
                    Intent intent = new Intent(getActivityInfo().getApplicationContext(),WeatherActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivityInfo().startActivity(intent);
                }
            }
        }

     /*   if((deltaYAbs >= MIN_SWIPE_DISTANCE_Y) && (deltaYAbs <= MAX_SWIPE_DISTANCE_Y))
        {
            if(deltaY > 0)
            {
                this.activity.displayMessage("Swipe to up");
            }else
            {
                this.activity.displayMessage("Swipe to down");
            }
        } */


        return true;
    }
/*
    // Invoked when single tap screen.
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        this.activity.displayMessage("Single tap occurred.");
        return true;
    }

    // Invoked when double tap screen.
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        this.activity.displayMessage("Double tap occurred.");
        return true;
    }
    */

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;


}