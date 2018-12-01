package com.example.asus.weathercast;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
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
import java.util.Calendar;
import java.util.Date;
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
    WeatherAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ConstraintLayout constraintLayout=findViewById(R.id.layout);
        AnimationDrawable animationDrawable =(AnimationDrawable)constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        weatherIcon=(ImageView)findViewById(R.id.imageView2);
        weatherDate=(TextView)findViewById(R.id.date);
        currentTemp=(TextView)findViewById(R.id.maxtemp);
        lowTemp=(TextView)findViewById(R.id.mintemp);
        cityCountry=(TextView)findViewById(R.id.location);
        weatherDescription=(TextView)findViewById(R.id.weathertype);

        RecyclerView recyclerview =(RecyclerView)findViewById(R.id.content_weather_reports);
        mAdapter =new WeatherAdapter(weatherReportList);
        recyclerview.setAdapter(mAdapter);
        LinearLayoutManager layoutmanager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerview.setLayoutManager(layoutmanager);




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
        Toast.makeText(this, fullCoords, Toast.LENGTH_LONG).show();

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
                    Log.v("listsize",Integer.toString(list.length()));

                    for(int i=0;i<list.length();i++){
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
                         i++;
                         i++;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.v("JSON","exec"+e.getLocalizedMessage());
                }
                updateUi();
                mAdapter.notifyDataSetChanged();


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
            weatherDate.setText("Today,"+report.getFormattedDate());
            Log.v("currentTIME",report.getFormattedDate());
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


    public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder>{

        private ArrayList<DailyWeatherReport> mDailyWeatherReport;

        public WeatherAdapter(ArrayList<DailyWeatherReport> mDailyWeatherReport) {
            this.mDailyWeatherReport = mDailyWeatherReport;
        }


        @Override
        public void onBindViewHolder(WeatherViewHolder holder, int position) {
            DailyWeatherReport report =mDailyWeatherReport.get(position);
            holder.updateUi(report);

        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemCount() {
            return mDailyWeatherReport.size();
        }

        @Override
        public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_weather,parent,false);
            return new WeatherViewHolder(card);
        }
    }



    public class WeatherViewHolder extends RecyclerView.ViewHolder {

        private ImageView weatherIcon;
        private TextView weatherDate;
        private TextView weatherDescription;
        private TextView tempHigh;
        private TextView tempLow;



        public WeatherViewHolder(View itemView) {
            super(itemView);
            weatherIcon = (ImageView) itemView.findViewById(R.id.weathericon);
            weatherDate=(TextView)itemView.findViewById(R.id.weather_day);
            weatherDescription =(TextView)itemView.findViewById(R.id.weather_description);
            tempHigh=(TextView)itemView.findViewById(R.id.weather_temp_high);
            tempLow=(TextView)itemView.findViewById(R.id.weather_temp_low);

        }


        public void updateUi(DailyWeatherReport report){
            Date currentTime = Calendar.getInstance().getTime();
            Log.v("datenow",report.getFormattedDate().substring(9,11));

               String today= (currentTime.toString()).substring(8,10);
               String apitoday=report.getFormattedDate().substring(9,11);

                int actualtime =Integer.valueOf(today);
                int apitime=Integer.valueOf(apitoday);

                if( actualtime == apitime){
                    weatherDate.setText("Today");

                }else if( apitime==actualtime+1){
                    weatherDate.setText("Tomorrow");
                }else{
                    weatherDate.setText(report.getFormattedDate());
                }


            weatherDescription.setText(report.getWeather());
            tempHigh.setText(Integer.toString(report.getMaxTemp())+"°C");
            tempLow.setText(Integer.toString(report.getMinTemp())+"°C");



            switch (report.getWeather()){
                case DailyWeatherReport.WEATHER_TYPE_CLOUDS:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.cloudy_mini));
                    break;
                case DailyWeatherReport.WEATHER_TYPE_RAIN:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.rainy_mini));
                case DailyWeatherReport.WEATHER_TYPE_SNOW:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.snow_mini));
                case DailyWeatherReport.WEATHER_TYPE_WIND:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.thunder_lightning_mini));
                default:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.sunny_mini));
            }
        }
    }

}











