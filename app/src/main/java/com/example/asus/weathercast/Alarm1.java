package com.example.asus.weathercast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Calendar;


public class Alarm1 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent1) {
        String  alarm = intent1.getStringExtra("alarm");

        Calendar calendar =Calendar.getInstance();
        Calendar calendar1 =Calendar.getInstance();
        calendar1.setTimeInMillis(Long.parseLong(alarm));

        int hournow=calendar.get(Calendar.HOUR);
        int houralarm=calendar1.get(Calendar.HOUR);

    if (houralarm == hournow) {


         Log.v("alarmsssss", "alarm is morning up" + hournow + "  " + houralarm);
    }







    }


}
