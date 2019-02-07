package com.example.asus.weathercast;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asus.weathercast.model.DailyWeatherReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static com.example.asus.weathercast.AppNotification.CHANNEL_1_ID;
import static com.example.asus.weathercast.SettingsActivity.myPref;


public class Alarm1 extends BroadcastReceiver {

    private NotificationManagerCompat notificationManager;
    ArrayList <DailyWeatherReport> weatherreport= new ArrayList<>();

    public Context mcontext;

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

    final String URL_BASE ="http://api.openweathermap.org/data/2.5/forecast";
    final String URL_COORD="/?lat="; // 51.502653&lon=0.0448183"
    final String URL_UNITS="&units=metric";
    final String URL_API_KEY= "&APPID=00bb81cd0d277258c793871a3a8b8b45";
    public Location location1;



    @Override
    public void onReceive(Context context, Intent intent1) {
        String  alarm = intent1.getStringExtra("alarm");
        notificationManager = NotificationManagerCompat.from(context);
        mcontext=context;



        SharedPreferences notif = context.getSharedPreferences(myPref,MODE_PRIVATE);
        String lat =notif.getString("LOCATION_LAT",String.valueOf(51.509865));
        String lon =notif.getString("LOCATION_LON",String.valueOf(-0.118092));
        String provider = notif.getString("LOCATION_PROVIDER",LocationManager.NETWORK_PROVIDER);

        location1 = new Location(provider);
        location1.setLatitude(Double.parseDouble(lat));
        location1.setLongitude(Double.parseDouble(lon));

        Log.v("PRIINTCATCHNOTCONNECTED","timehascome");

        downloadWeatherData(location1,alarm);







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
    public void downloadWeatherData(Location location,String alarm){
        final String fullCoords= URL_COORD +location.getLatitude() + "&lon=" + location.getLongitude();
        //final String fullCoords=URL_COORD +71.2080+ "&lon=" +46.8139 ; // for testing purposes
        final String url = URL_BASE +fullCoords+URL_UNITS+URL_API_KEY;
        final String alarm1=alarm;
        Log.v("WEATHERDEBUG","url: "+url);
             final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.v("WEATHERDEBUG", "res:" + response.toString());

                    try {
                        JSONObject city = response.getJSONObject("city");
                        String cityName = city.getString("name");
                        String country = city.getString("country");
                        Log.v("JSON", "name" + cityName + "country" + country);

                        JSONArray list = response.getJSONArray("list");
                        Log.v("listsize", Integer.toString(list.length()));
                        weatherreport.clear();

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject obj = list.getJSONObject(i);
                            JSONObject main = obj.getJSONObject("main");
                            Double maxTemp = main.getDouble("temp_max");
                            Double minTemp = main.getDouble("temp_min");
                            JSONArray weatherArr = obj.getJSONArray("weather");
                            JSONObject weather = weatherArr.getJSONObject(0);
                            String weatherType = weather.getString("main");
                            String rawDate = obj.getString("dt_txt");


                            DailyWeatherReport report1 = new DailyWeatherReport(maxTemp.intValue(), minTemp.intValue(), weatherType, rawDate);
                            weatherreport.add(report1);

                        }
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
                        calendar1.setTimeInMillis(Long.parseLong(alarm1));

                        int hournow=calendar.get(Calendar.HOUR);
                        int houralarm=calendar1.get(Calendar.HOUR);



                        if (houralarm == hournow) {

                            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Notification notification = new NotificationCompat.Builder(mcontext, CHANNEL_1_ID)
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.v("PRIINTCATCHNOTCONNECTED", "exec" + e.getLocalizedMessage());
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("PRIINTCATCHNOTCONNECTED", "error" + error.getLocalizedMessage());
                    Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Notification notification = new NotificationCompat.Builder(mcontext, CHANNEL_1_ID)
                            .setSmallIcon(R.drawable.umbrella)
                            .setContentTitle("Your Weather report")
                            .setContentText("No internet")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                            .setLights(Color.RED, 3000, 3000)
                            .setSound(uri)
                            .build();

                    notificationManager.notify(1, notification);

                }
            });

            Volley.newRequestQueue(mcontext).add(jsonRequest);

        }



}


