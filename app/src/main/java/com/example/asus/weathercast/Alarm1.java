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
    ArrayList <DailyWeatherReport> weatherreport= new ArrayList<>();

    String nxtweather1;
    String nxtweather2;
    String nxtweather3;
    int maxtemp1;
    int maxtemp2;
    int maxtemp3;
    int mintemp1;
    int mintemp2;
    int mintemp3;
    int averagemintemp;
    int averagemaxtemp;
    String nxtweather;


    @Override
    public void onReceive(Context context, Intent intent1) {
        String  alarm = intent1.getStringExtra("alarm");
        notificationManager = NotificationManagerCompat.from(context);


        weatherreport = WeatherActivity.getWeatherReportList();

        if(weatherreport.size()>0){
            DailyWeatherReport report=weatherreport.get(0);
            DailyWeatherReport report1=weatherreport.get(1);
            DailyWeatherReport report2=weatherreport.get(2);
            nxtweather1=report.getWeather();
            nxtweather2=report1.getWeather();
            nxtweather3=report2.getWeather();
            maxtemp1=report.getMaxTemp();
            maxtemp2=report1.getMaxTemp();
            maxtemp3=report2.getMaxTemp();
            mintemp1=report.getMinTemp();
            mintemp2=report1.getMinTemp();
            mintemp3=report2.getMinTemp();

            averagemaxtemp=averagetempmax(maxtemp1,maxtemp2,maxtemp3);
            averagemintemp=averagetempmin(mintemp1,mintemp2,mintemp3);
            nxtweather=nxtweather(nxtweather1,nxtweather2,nxtweather3);

        }

        Calendar calendar =Calendar.getInstance();
        Calendar calendar1 =Calendar.getInstance();
        calendar1.setTimeInMillis(Long.parseLong(alarm));

        int hournow=calendar.get(Calendar.HOUR);
        int houralarm=calendar1.get(Calendar.HOUR);

    if (houralarm == hournow) {

         Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.umbrella)
                .setContentTitle("Your Weather report")
                .setContentText(nxtweather+" "+averagemintemp+" "+averagemaxtemp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setSound(uri)
                .build();

        notificationManager.notify(1, notification);

    }







    }

    private String nxtweather(String nxtweather1, String nxtweather2, String nxtweather3) {
        String result="";
        if((nxtweather1.equals("Rain")) || (nxtweather2.equals("Rain")) ||(nxtweather3.equals("Rain"))) {
            result="Possibility of rain,better take your umbrella with you ";
            return result;
        }else if ((nxtweather1.equals("Snow")) || (nxtweather2.equals("Snow")) ||(nxtweather3.equals("Snow"))){
            result="Possibility of snow,better take your umbrella and heavy clothing";
            return result;
        }
        else if ((nxtweather1.equals("Clouds")) || (nxtweather2.equals("Clouds")) ||(nxtweather3.equals("Clouds"))){
            result="The sky is full of clouds";
            return result;
        }else if ((nxtweather1.equals("Wind")) || (nxtweather2.equals("Wind")) ||(nxtweather3.equals("Wind"))){
            result="There is winds the coming hours";
            return result;
        }else{
            result="It is clear sky for now";
            return result;
        }
     }

    private int averagetempmin(int mintemp1, int mintemp2, int mintemp3) {
        int result;
        result=(mintemp1+mintemp2+mintemp3)/3;
        return result;
    }

    private int averagetempmax(int maxtemp1, int maxtemp2, int maxtemp3) {
        int result;
        result=(maxtemp1+maxtemp2+maxtemp3)/3;
        return result;
    }


}
