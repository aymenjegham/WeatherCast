package com.example.asus.weathercast;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asus.weathercast.model.DailyWeatherReport;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.jar.JarException;

public class WeatherActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks,LocationListener {

    final String URL_BASE ="http://api.openweathermap.org/data/2.5/forecast";
    final String URL_COORD="/?lat="; // 51.502653&lon=0.0448183"
    final String URL_UNITS="&units=metric";
    final String URL_API_KEY= "&APPID=00bb81cd0d277258c793871a3a8b8b45";

    private GoogleApiClient mGoogleApiClient;
    private final int PERMISSION_LOCATION=111;
    private ArrayList<DailyWeatherReport> weatherReportList= new ArrayList<>();

    private ImageView weatherIcon;
    private ImageView weatherIconMini;
    private TextView weatherDate;
    private TextView currentTemp;
    private TextView lowTemp;
    private TextView cityCountry;
    private TextView weatherDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherIcon=(ImageView)findViewById(R.id.imageView2);
        weatherDate=(TextView)findViewById(R.id.date);
        currentTemp=(TextView)findViewById(R.id.maxtemp);
        lowTemp=(TextView)findViewById(R.id.mintemp);
        cityCountry=(TextView)findViewById(R.id.location);
        weatherDescription=(TextView)findViewById(R.id.weathertype);


        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this,this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    public void downloadWeatherData(Location location){
        final String fullCoords= URL_COORD +location.getLatitude() + "&lon=" + location.getLongitude();
        final String url = URL_BASE +fullCoords+URL_UNITS+URL_API_KEY;
        Log.v("WEATHERDEBUG","url: "+url);

        final JsonObjectRequest jsonRequest =new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("WEATHERDEBUG","res:"+response.toString());

                try{
                    JSONObject city= response.getJSONObject("city");
                    String cityName =city.getString("name");
                    String country=city.getString("country");
                    Log.v("JSON","name"+cityName+"country"+ country);

                    JSONArray list =response.getJSONArray("list");

                    for(int i=0;i<5;i++){
                        JSONObject obj =list.getJSONObject(i);
                        JSONObject main =obj.getJSONObject("main");
                        Double currentTemp =main.getDouble("temp");
                        Double maxTemp=main.getDouble("temp_max");
                        Double minTemp=main.getDouble("temp_min");

                        JSONArray weatherArr=obj.getJSONArray("weather");
                        JSONObject weather =weatherArr.getJSONObject(0);
                        String weatherType =weather.getString("main");

                        String rawDate=obj.getString("dt_txt");

                        DailyWeatherReport report =new DailyWeatherReport(cityName,country,currentTemp.intValue(),maxTemp.intValue(),minTemp.intValue(),weatherType,rawDate);
                        Log.v("JSON","printing from class "+rawDate);
                        weatherReportList.add(report);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.v("JSON","exec"+e.getLocalizedMessage());
                }
                updateUi();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("WEATHERDEBUG","error"+error.getLocalizedMessage());
            }
        });

        Volley.newRequestQueue(this).add(jsonRequest);
    }


    public void updateUi(){
        if(weatherReportList.size()>0){
            DailyWeatherReport report=weatherReportList.get(0);
            switch (report.getWeather()){
                case DailyWeatherReport.WEATHER_TYPE_CLOUDS:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.cloudy));
                    break;
                case DailyWeatherReport.WEATHER_TYPE_RAIN:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.rainy));
                case DailyWeatherReport.WEATHER_TYPE_SNOW:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.snow));
                case DailyWeatherReport.WEATHER_TYPE_WIND:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.thunder_lightning));
                default:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.sunny));
            }
            weatherDate.setText("Today,November 30");
            currentTemp.setText(Integer.toString(report.getMaxTemp())+"°C");
            lowTemp.setText(Integer.toString(report.getMinTemp())+"°C");
            cityCountry.setText(report.getCityName()+", "+ report.getCountry());
            weatherDescription.setText(report.getWeather());
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_LOCATION);
        } else{
            startLocationServices();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        downloadWeatherData(location);
    }
    public void startLocationServices(){
        Log.v("Debugging","startedlocationservices started");
        try{
            LocationRequest req = LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, req, this);
            Log.v("Debugging", "Requesting location updates");
        }catch (SecurityException exception){
            Log.v("Debugging","permission is needed for getting location");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_LOCATION: {
                Log.v("Debugging", "Permission Granted - starting services");

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationServices();
                } else {
                    //show a dialog saying something like, "I can't run your location dummy - you denied permission!"
                    Log.v("Debugging", "Permission not granted");
                    Toast.makeText(this, "Permission not Granted", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

}
