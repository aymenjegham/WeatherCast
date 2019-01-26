package com.example.asus.weathercast;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity  {


    private GestureDetectorCompat gestureDetectorCompat = null;
    Context mContext;

    Time time;
    long [] alarms;
    TextView alarm1;
    TextView alarm2;
    TextView alarm3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //gesture swipe to left and to right
        DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener();
        gestureListener.setActivity(this);
        gestureDetectorCompat = new GestureDetectorCompat(this, gestureListener);



        alarm1=findViewById(R.id.textClock);
        alarm2=findViewById(R.id.textClock2);
        alarm3=findViewById(R.id.textClock3);

        alarm1.setText("07:00 AM");
        alarm2.setText("12:30 PM");
        alarm3.setText("05:00 PM");

        alarm1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment newFragment = TimePickerFragment.newInstance(1);
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });

        alarm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment newFragment = TimePickerFragment.newInstance(2);
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });
        alarm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment newFragment = TimePickerFragment.newInstance(3);
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });

      /*  //Alarm listener
        btnalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar =Calendar.getInstance();
                calendar.set(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        timePicker.getHour(),
                        timePicker.getMinute(),
                        0);
                alarms = new long[]{calendar.getTimeInMillis()+458, calendar.getTimeInMillis()+14,calendar.getTimeInMillis()};

                setAlarm(alarms);//calendar.getTimeInMillis());
            }

        });
*/

    }

    private void setAlarm(long [] timeInMillis) {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this,Alarms.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        for(int i =0;i<timeInMillis.length;i++){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,timeInMillis[i],AlarmManager.INTERVAL_DAY,pendingIntent);
            Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
            long time =timeInMillis[i];
         }







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



