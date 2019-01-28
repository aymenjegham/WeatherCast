package com.example.asus.weathercast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Alarm1 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent1) {
        String  alarm = intent1.getStringExtra("alarm");

        Log.v("alarmsssss","alarm is morning up"+alarm);
    }

}
