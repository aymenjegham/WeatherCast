package com.example.asus.weathercast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Alarms extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("alarmsssss","alarm is up");
    }
}
