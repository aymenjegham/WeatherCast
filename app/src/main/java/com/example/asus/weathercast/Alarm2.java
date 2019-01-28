package com.example.asus.weathercast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Alarm2 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent2) {
        Log.v("alarmsssss","alarm afternoon is up");
    }

}