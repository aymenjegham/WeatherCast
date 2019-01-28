package com.example.asus.weathercast;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;


public class SettingsActivity extends AppCompatActivity  {


    private GestureDetectorCompat gestureDetectorCompat = null;
    Context mContext;

    Time time;
    String editedTime;
    long [] alarms;
    TextView alarm1;
    TextView alarm2;
    TextView alarm3;
    String hour;
    String minute;
    String amorpm;
    String hourandminute;
    long alarmarray[] = new long[2];



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

         alarm1.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
             }

             @Override
             public void afterTextChanged(Editable s) {
                 editedTime= s.toString();
                 String[] hourandminuteandpm =editedTime.split(" ",editedTime.length());
                 amorpm=hourandminuteandpm[1];
                 hourandminute=hourandminuteandpm[0];
                 String[] hour_minute= hourandminute.split(":",hourandminute.length());
                 hour =hour_minute[0];
                 minute=hour_minute[1];
                 addToAlarm1(hour,minute,amorpm);
             }
         });
         alarm2.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
             }

             @Override
             public void afterTextChanged(Editable s) {
                 editedTime= s.toString();
                 String[] hourandminuteandpm =editedTime.split(" ",editedTime.length());
                 amorpm=hourandminuteandpm[1];
                 hourandminute=hourandminuteandpm[0];
                 String[] hour_minute= hourandminute.split(":",hourandminute.length());
                 hour =hour_minute[0];
                 minute=hour_minute[1];
                 addToAlarm2(hour,minute,amorpm);
             }
         });

         alarm3.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
             }

             @Override
             public void afterTextChanged(Editable s) {
                 editedTime= s.toString();
                 String[] hourandminuteandpm =editedTime.split(" ",editedTime.length());
                 amorpm=hourandminuteandpm[1];
                 hourandminute=hourandminuteandpm[0];
                 String[] hour_minute= hourandminute.split(":",hourandminute.length());
                 hour =hour_minute[0];
                 minute=hour_minute[1];
                 addToAlarm3(hour,minute,amorpm);
             }
         });

    }
    public void addToAlarm1(String hour,String minute,String am_pm){
        switch (am_pm) {
            case "PM":
                int h;
                h=Integer.parseInt(hour);
                if(h<12){
                    hour=String.valueOf(h+12);
                }else {
                    hour=String.valueOf(h);
                }
            case "AM":
                int j;
                j=Integer.parseInt(hour);
                if(j ==12){
                    hour=String.valueOf(0);
                }else {
                    hour=String.valueOf(j);
                }
        }
        Calendar calendar =Calendar.getInstance();
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                Integer.parseInt(hour),
                Integer.parseInt(minute),
                0);
        setAlarm1(calendar.getTimeInMillis());
    }
    public void addToAlarm2(String hour,String minute,String am_pm){
        switch (am_pm) {
            case "PM":
                int h;
                h=Integer.parseInt(hour);
                if(h<12){
                    hour=String.valueOf(h+12);
                }else {
                    hour=String.valueOf(h);
                }
            case "AM":
                int j;
                j=Integer.parseInt(hour);
                if(j ==12){
                    hour=String.valueOf(0);
                }else {
                    hour=String.valueOf(j);
                }
        }
        Calendar calendar =Calendar.getInstance();
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                Integer.parseInt(hour),
                Integer.parseInt(minute),
                0);
        setAlarm2(calendar.getTimeInMillis());
    }
    public void addToAlarm3(String hour,String minute,String am_pm){
        switch (am_pm) {
            case "PM":
                int h;
                h=Integer.parseInt(hour);
                if(h<12){
                    hour=String.valueOf(h+12);
                }else {
                    hour=String.valueOf(h);
                }
            case "AM":
                int j;
                j=Integer.parseInt(hour);
                if(j ==12){
                    hour=String.valueOf(0);
                }else {
                    hour=String.valueOf(j);
                }
        }
        Calendar calendar =Calendar.getInstance();
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                Integer.parseInt(hour),
                Integer.parseInt(minute),
                0);
        setAlarm3(calendar.getTimeInMillis());
    }

    private void setAlarm1(long timeInMillis) {
        AlarmManager alarmManager1 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(this,Alarm1.class);
        intent1.putExtra("alarm", "alarm1");
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this,1,intent1,0);
        alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP,timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent1);
        Toast.makeText(this, "Alarm 1 is set", Toast.LENGTH_SHORT).show();
    }

    private void setAlarm2(long timeInMillis) {
        AlarmManager alarmManager2 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent2 = new Intent(this,Alarm1.class);
        intent2.putExtra("alarm", "alarm2");
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this,2,intent2,0);
        alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP,timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent2);
        Toast.makeText(this, "Alarm 2 is set", Toast.LENGTH_SHORT).show();
    }
    private void setAlarm3(long timeInMillis) {
        AlarmManager alarmManager3 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent3 = new Intent(this,Alarm1.class);
        intent3.putExtra("alarm", "alarm3");
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(this,3,intent3,0);
        alarmManager3.setRepeating(AlarmManager.RTC_WAKEUP,timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent3);
        Toast.makeText(this, "Alarm 3 is set", Toast.LENGTH_SHORT).show();
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



