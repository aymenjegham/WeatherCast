package com.example.asus.weathercast;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
 import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;


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
    public static final String myPref = "preferenceName";
    Switch aSwitch;
    Switch aSwitch2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        alarm1=findViewById(R.id.textClock);
        alarm2=findViewById(R.id.textClock2);
        alarm3=findViewById(R.id.textClock3);
        aSwitch=findViewById(R.id.switch1);
        aSwitch2=findViewById(R.id.switch2);



        //gesture swipe to left and to right
        DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener();
        gestureListener.setActivity(this);
        gestureDetectorCompat = new GestureDetectorCompat(this, gestureListener);

        SharedPreferences notif = getSharedPreferences(myPref,MODE_PRIVATE);
        String state = notif.getString("ON/OFF","ON");
         String alarm_1 = notif.getString("alarm1","07:00 AM");
        String alarm_2 = notif.getString("alarm2","07:00 AM");
         String alarm_3 = notif.getString("alarm3","07:00 AM");
        String initial = notif.getString("initiallaunch","yes");

        Log.v("offmonitoring","here:"+state);



        if(initial=="yes"){
            Calendar calendar =Calendar.getInstance();
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    7,
                    0,
                    0);
            setAlarm1(calendar.getTimeInMillis());
            SharedPreferences.Editor editor3 = getSharedPreferences(myPref,Context.MODE_PRIVATE).edit();
            editor3.putString("initiallaunch", "no");
            editor3.commit();
        }



        if(state.equals("ON")){
            Log.v("offmonitoring","passed");

            aSwitch.setChecked(true);
            alarm1.setText(alarm_1);
            alarm2.setText(alarm_2);
            alarm3.setText(alarm_3);


        }else {

            alarm1.setText(alarm_1);
            alarm2.setText(alarm_2);
            alarm3.setText(alarm_3);

            aSwitch.setChecked(false);
            AlarmManager alarmManager1 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent1 = new Intent(getApplicationContext(),Alarm1.class);
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(),1,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent1.cancel();
            alarmManager1.cancel(pendingIntent1);
            alarm1.setTextColor(Color.GRAY);
            alarm1.setEnabled(false);

            //-----

            AlarmManager alarmManager2 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent2 = new Intent(getApplicationContext(),Alarm1.class);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(),2,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent2.cancel();
            alarmManager2.cancel(pendingIntent2);
            alarm2.setTextColor(Color.GRAY);
            alarm2.setEnabled(false);

            //----

            AlarmManager alarmManager3 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent3 = new Intent(getApplicationContext(),Alarm1.class);
            PendingIntent pendingIntent3 = PendingIntent.getBroadcast(getApplicationContext(),3,intent3,PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent3.cancel();
            alarmManager3.cancel(pendingIntent3);
            alarm3.setTextColor(Color.GRAY);
            alarm3.setEnabled(false);


        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(!isChecked){
                   Toast.makeText(getApplicationContext(), "Notification alarms disabled", Toast.LENGTH_SHORT).show();
                   AlarmManager alarmManager1 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                   Intent intent1 = new Intent(getApplicationContext(),Alarm1.class);
                   PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(),1,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
                   pendingIntent1.cancel();
                   alarmManager1.cancel(pendingIntent1);
                   alarm1.setTextColor(Color.GRAY);
                   alarm1.setEnabled(false);

                   //-----------

                   AlarmManager alarmManager2 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                   Intent intent2 = new Intent(getApplicationContext(),Alarm1.class);
                   PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(),2,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
                   pendingIntent2.cancel();
                   alarmManager2.cancel(pendingIntent2);
                   alarm2.setTextColor(Color.GRAY);
                   alarm2.setEnabled(false);

                   //-------------

                   AlarmManager alarmManager3 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                   Intent intent3 = new Intent(getApplicationContext(),Alarm1.class);
                   PendingIntent pendingIntent3 = PendingIntent.getBroadcast(getApplicationContext(),3,intent3,PendingIntent.FLAG_UPDATE_CURRENT);
                   pendingIntent3.cancel();
                   alarmManager3.cancel(pendingIntent3);
                   alarm3.setTextColor(Color.GRAY);
                   alarm3.setEnabled(false);
                  // ------

                   SharedPreferences.Editor editor = getSharedPreferences(myPref,Context.MODE_PRIVATE).edit();
                   editor.putString("ON/OFF", "OFF");
                   Log.v("offmonitoring","off1");
                   editor.commit();

               }else{
                   alarm1.setTextColor(getColor(R.color.colorPrimaryDark));
                   alarm1.setEnabled(true);
                   Calendar calendar =Calendar.getInstance();
                   calendar.set(
                           calendar.get(Calendar.YEAR),
                           calendar.get(Calendar.MONTH),
                           calendar.get(Calendar.DAY_OF_MONTH),
                           7,
                           0,
                           0);
                   SharedPreferences notif = getSharedPreferences(myPref,MODE_PRIVATE);
                   long lastalarm1 = notif.getLong("lastalarm1",calendar.getTimeInMillis());
                   Log.v("lastalarm",String.valueOf(lastalarm1));

                   setAlarm1(lastalarm1);

                   //-----

                   alarm2.setTextColor(getColor(R.color.colorPrimaryDark));
                   alarm2.setEnabled(true);
                   long lastalarm2 = notif.getLong("lastalarm2",calendar.getTimeInMillis());
                   Log.v("lastalarm",String.valueOf(lastalarm2));

                   setAlarm2(lastalarm2);

                   //---

                   alarm3.setTextColor(getColor(R.color.colorPrimaryDark));
                   alarm3.setEnabled(true);
                   long lastalarm3 = notif.getLong("lastalarm3",calendar.getTimeInMillis());
                   Log.v("lastalarm",String.valueOf(lastalarm3));

                   setAlarm3(lastalarm3);

                   //---

                   SharedPreferences.Editor editor = getSharedPreferences(myPref,Context.MODE_PRIVATE).edit();
                   editor.putString("ON/OFF","ON");
                   Log.v("offmonitoring","ON1");

                   editor.commit();

               }
            }
        });









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

        SharedPreferences shared = getSharedPreferences(myPref,MODE_PRIVATE);
        String GPSenabled = shared.getString("GPSenabled","TRUE");

        if(GPSenabled.equals("TRUE")){
            aSwitch2.setChecked(true);
        }else {
            aSwitch2.setChecked(false);
        }




         aSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                 if(!isChecked){
                     SharedPreferences.Editor editor = getSharedPreferences(myPref,getApplicationContext().MODE_PRIVATE).edit();
                     editor.putString("GPSenabled","FALSE");
                     editor.commit();
                     new MapDialogFragment().show(getSupportFragmentManager(), null);


                 }else{
                     SharedPreferences.Editor editor = getSharedPreferences(myPref,getApplicationContext().MODE_PRIVATE).edit();
                     editor.putString("GPSenabled","TRUE");
                     editor.commit();
                 }

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
        SharedPreferences.Editor editor1 = getSharedPreferences(myPref,Context.MODE_PRIVATE).edit();
        editor1.putLong("lastalarm1", calendar.getTimeInMillis());
        editor1.commit();
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
        SharedPreferences.Editor editor1 = getSharedPreferences(myPref,Context.MODE_PRIVATE).edit();
        editor1.putLong("lastalarm2", calendar.getTimeInMillis());
        editor1.commit();
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
        SharedPreferences.Editor editor1 = getSharedPreferences(myPref,Context.MODE_PRIVATE).edit();
        editor1.putLong("lastalarm3", calendar.getTimeInMillis());
        editor1.commit();
    }

    private void setAlarm1(long timeInMillis) {
        AlarmManager alarmManager1 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(this,Alarm1.class);
        intent1.putExtra("alarm", String.valueOf(timeInMillis));
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this,1,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP,timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent1);
        Toast.makeText(this, "Alarm 1 is set", Toast.LENGTH_SHORT).show();
    }

    private void setAlarm2(long timeInMillis) {
        AlarmManager alarmManager2 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent2 = new Intent(this,Alarm1.class);
        intent2.putExtra("alarm", String.valueOf(timeInMillis));
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this,2,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP,timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent2);
        Toast.makeText(this, "Alarm 2 is set", Toast.LENGTH_SHORT).show();
    }
    private void setAlarm3(long timeInMillis) {
        AlarmManager alarmManager3 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent3 = new Intent(this,Alarm1.class);
        intent3.putExtra("alarm", String.valueOf(timeInMillis));
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(this,3,intent3,PendingIntent.FLAG_UPDATE_CURRENT);
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



