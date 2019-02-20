package com.example.asus.weathercast.model;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
 import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asus.weathercast.R;
import com.example.asus.weathercast.WeatherActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ASUS on 29/11/2018.
 */

public class DailyWeatherReport {




     public static final  String  WEATHER_TYPE_CLOUDS="Clouds";
    public static final  String  WEATHER_TYPE_CLEAR="Clear";
    public static final  String  WEATHER_TYPE_RAIN="Rain";
    public static final  String  WEATHER_TYPE_WIND="Wind";
    public static final  String  WEATHER_TYPE_SNOW="Snow";

    private String cityName;
    private String country;
    private int currentTemp;
    private int maxTemp;
    private int minTemp;
    private String weather;
    private String formattedDate;

    public String getRawdate() {
        return rawdate;
    }

    private String rawdate;


    public DailyWeatherReport(String cityName, String country, int currentTemp, int maxTemp, int minTemp, String weather, final String rawDate, Location location, Context mcontext) {
        this.cityName = cityName;
        this.country = country;
        this.currentTemp = currentTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.weather = weather;
        this.formattedDate = rawDateToReadable(rawDate);
        this.rawdate= rawDate;








    }

     public DailyWeatherReport(int maxTemp, int minTemp, String weather, String rawdate) {
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.weather = weather;
        this.rawdate=rawdate;

    }
    public String gmtToLocal(String rawDate,Long offset){

        Log.v("offsetcheckpoint",""+offset);


        return "2019-02-24 18:00:00";

    }

    public String rawDateToReadable(String rawDate){
        String oldstring = rawDate;
        String month="";
       switch (rawDate.substring(5,7)){
           case("11"):
               month="November";
               break;
           case("12"):
               month="December";
               break;
           case("01"):
               month="January";
               break;
           case("02"):
               month="February";
               break;
           case("03"):
               month="March";
               break;
           case("04"):
               month="April";
               break;
           case("05"):
               month="May";
               break;
           case("06"):
               month="June";
               break;
           case("07"):
               month="July";
               break;
           case("08"):
               month="August";
               break;
           case("09"):
               month="September";
               break;
           case("10"):
               month="October";
               break;


      }

       Log.v("rawDATA",month+" "+rawDate );

        return month+" "+rawDate.substring(8,10);
    }


    public String getCityName() {
        return cityName;
    }

    public String getCountry() {
        return country;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public String getWeather() {
        return weather;
    }

    public String getFormattedDate() {
        return formattedDate;
    }
}
