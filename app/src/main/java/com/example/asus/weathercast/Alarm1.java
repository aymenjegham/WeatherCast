package com.example.asus.weathercast;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.asus.weathercast.model.DailyWeatherReport;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.asus.weathercast.AppNotification.CHANNEL_1_ID;


public class Alarm1 extends BroadcastReceiver {

    private NotificationManagerCompat notificationManager;

    @Override
    public void onReceive(Context context, Intent intent1) {
        String  alarm = intent1.getStringExtra("alarm");
        notificationManager = NotificationManagerCompat.from(context);

        Log.v("myweather3",WeatherActivity.getWeatherReportList().toString());




        Calendar calendar =Calendar.getInstance();
        Calendar calendar1 =Calendar.getInstance();
        calendar1.setTimeInMillis(Long.parseLong(alarm));

        int hournow=calendar.get(Calendar.HOUR);
        int houralarm=calendar1.get(Calendar.HOUR);

    if (houralarm == hournow) {


         Log.v("alarmsssss", "alarm is morning up" + hournow + "  " + houralarm);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.umbrella)
                .setContentTitle("testing")
                .setContentText("bring your umbrella with you ,it is raining today")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setSound(uri)
                .build();

        notificationManager.notify(1, notification);

    }







    }


}
