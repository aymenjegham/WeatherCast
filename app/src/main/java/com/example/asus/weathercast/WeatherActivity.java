package com.example.asus.weathercast;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.jar.JarException;

import static com.example.asus.weathercast.SettingsActivity.myPref;
import static java.security.AccessController.getContext;

public class WeatherActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks,LocationListener,View.OnClickListener {

    final String URL_BASE ="http://api.openweathermap.org/data/2.5/forecast";
    final String URL_COORD="/?lat="; // 51.502653&lon=0.0448183"
    final String URL_UNITS="&units=metric";
    final String URL_API_KEY= "&APPID=00bb81cd0d277258c793871a3a8b8b45";

    private GoogleApiClient mGoogleApiClient;
    private final int PERMISSION_LOCATION=111;
    static private ArrayList<DailyWeatherReport> weatherReportList= new ArrayList<>();

      public Location location1;
      public Location  LocationGl;
      String GPSenabled;

    boolean state=true;
    int ctof,ftoc,ctof2,ftoc2;
    public String statevalue2;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;

    private ConstraintLayout layout;



    private ResideMenu resideMenu;
    private ResideMenuItem resideMenuSetting;
    private ResideMenuItem resideMenuHome;
    private ResideMenuItem resideMenuNew;

    private Context mcontext;





    private ImageView weatherIcon;
    private ImageView weatherIconMini;
    private ImageView sadface ;
    private TextView weatherDate;
    private TextView currentTemp;
    private TextView lowTemp;
    private TextView cityCountry;
    private TextView weatherDescription;
    private TextView msgint1;
    private TextView msgint2;
    WeatherAdapter mAdapter;
    SharedPreferences sharedPreferences;
    Context context;
    LocationManager locationManager;





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
        sadface=(ImageView)findViewById(R.id.internetlost);
        sadface.setVisibility(View.INVISIBLE);
        msgint1=(TextView)findViewById(R.id.internetlostmsg);
        msgint1.setVisibility(View.INVISIBLE);
        msgint2=(TextView)findViewById(R.id.internetlostmsg2);
        layout=(ConstraintLayout)findViewById(R.id.layout) ;
        msgint2.setVisibility(View.INVISIBLE);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadWeatherData(LocationGl);
                pullToRefresh.setRefreshing(false);
            }


        });

        setUpMenu();



        SharedPreferences shared = getSharedPreferences(myPref,MODE_PRIVATE);
        GPSenabled = shared.getString("GPSenabled","TRUE");
        Log.v("Gpsenabled",GPSenabled);




        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!isLocationEnabled()){
             displayLocationSettingsRequest(this);
        }



        RecyclerView recyclerview =(RecyclerView)findViewById(R.id.content_weather_reports);
        mAdapter =new WeatherAdapter(weatherReportList);
        recyclerview.setAdapter(mAdapter);
        LinearLayoutManager layoutmanager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerview.setLayoutManager(layoutmanager);

        ConnectivityManager cm = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        Log.v("checkinternet",String.valueOf( ni));
        if(ni ==null){
                sadface.setVisibility(View.VISIBLE);
                sadface.setImageDrawable(getResources().getDrawable(R.drawable.sadface));
                msgint1.setVisibility(View.VISIBLE);
                msgint2.setVisibility(View.VISIBLE);
                sadface.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        overridePendingTransition( 0, 0);
                        startActivity(getIntent());
                        overridePendingTransition( 0, 0);
                    }
                });

        }






        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this,this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();



    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    private void setUpMenu() {

        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.gradient_1);
        resideMenu.attachToActivity(this);
         resideMenu.setScaleValue(0.7f);
        resideMenuSetting =new ResideMenuItem(this,R.drawable.ic_settings,"Settings");
        resideMenuHome =new ResideMenuItem(this,R.drawable.ic_home,"Home");
        resideMenuNew =new ResideMenuItem(this,R.drawable.ic_info,"Info");

        resideMenuSetting.setOnClickListener(this);
        resideMenuHome.setOnClickListener(this);
        resideMenuNew.setOnClickListener(this);


        resideMenu.setMenuListener(menuListener);
       resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
      resideMenu.addMenuItem(resideMenuSetting,ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(resideMenuHome,ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(resideMenuNew,ResideMenu.DIRECTION_LEFT);

    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
         }
        @Override
        public void closeMenu() {
            //resideMenu.removeView(resideMenuSetting);
         }
    };


    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000/ 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("LOCATIONPROMPT", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("LOCATIONPROMPT", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(WeatherActivity.this, REQUEST_CHECK_SETTINGS);
                            Log.i("LOCATIONPROMPT","connected");
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("LOCATIONPROMPT", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("LOCATIONPROMPT", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode){
            case -1:
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);
                break;
            case 0:
                break;

        }


    }

    /*
    private void showAlert(final int status) {
        String message, title, btnText;
        if (status == 1) {
            message = "Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                    "use this app";
            title = "Enable Location";
            btnText = "Location Settings";
        } else {
            message = "Please allow this app to access location!";
            title = "Permission access";
            btnText = "Grant";
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (status == 1) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }
*/





    public void updateUi(){
        if(weatherReportList.size()>0){
            DailyWeatherReport report=weatherReportList.get(0);
            Date currentTime = Calendar.getInstance().getTime();
            String ct=currentTime.toString().substring(11,13);
            int time=Integer.valueOf(ct);


            switch (report.getWeather()){
                case (DailyWeatherReport.WEATHER_TYPE_CLOUDS) :
                    if(time <18 && time> 06){
                        weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.clouds));
                    }else{
                        weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.cloud_night));}

                    break;
                case DailyWeatherReport.WEATHER_TYPE_RAIN:
                    if(time <18 && time> 06){
                        weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.rain_sun));
                     }else{
                        weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.rain_night));
                     }
                        break;
                case DailyWeatherReport.WEATHER_TYPE_SNOW:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.snow));
                    break;
                case DailyWeatherReport.WEATHER_TYPE_WIND:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.thunder_lightning));
                    break;
                case DailyWeatherReport.WEATHER_TYPE_CLEAR:
                    if(time <18 && time> 06){
                        weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.sun));
                    }else{
                        weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.moon));}
                        break;
                default:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.moon));
            }

                Log.v("reportformatteddate",report.getRawdate().substring(8,10));
            String dateMonth="";
            String dateDay=report.getRawdate().substring(8,10);
            String month= (report.getFormattedDate().substring(0,3));
            Log.v("reportformatteddate2",report.getFormattedDate().substring(0,3));

            switch(month){
                case "Jan":
                    dateMonth=getString(R.string.january);
                    break;
                case "Feb":
                    dateMonth=getString(R.string.february);
                    break;
                case "Mar":
                    dateMonth=getString(R.string.march);
                    break;
                case "Apr":
                    dateMonth=getString(R.string.april);
                    break;
                case "May":
                    dateMonth=getString(R.string.may);
                    break;
                case "Jun":
                    dateMonth=getString(R.string.june);
                    break;
                case "Jul":
                    dateMonth=getString(R.string.july);
                    break;
                case "Aug":
                    dateMonth=getString(R.string.august);
                    break;
                case "Sep":
                    dateMonth=getString(R.string.september);
                    break;
                case "Oct":
                    dateMonth=getString(R.string.october);
                    break;
                case "Nov":
                    dateMonth=getString(R.string.november);
                    break;
                case "Dec":
                    dateMonth=getString(R.string.december);
                    break;
                default:
                    dateMonth= month;
            }
            Log.v("currentTIME", Locale.getDefault().getDisplayLanguage());
            String languageUsed=Locale.getDefault().getDisplayLanguage();

            switch (languageUsed){
                case "français":
                    weatherDate.setText(getString(R.string.today)+dateDay+" "+dateMonth);
                    break;
                case  "English":
                    weatherDate.setText(getString(R.string.today)+dateMonth+" "+dateDay);
                    break;
                default:
                    weatherDate.setText(getString(R.string.today)+dateMonth+" "+dateDay);

            }


            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String stateValue = sharedPreferences.getString("STATE", "C");

            ctof =  (report.getMaxTemp()*9/5)+32;
            ftoc=report.getMaxTemp();
            ctof2 =(report.getMinTemp()*9/5)+32;
            ftoc2=report.getMinTemp();

            switch(stateValue){
                case "C":

                currentTemp.setText(Integer.toString(report.getMaxTemp())+"°C");
                lowTemp.setText(Integer.toString(report.getMinTemp())+"°C");
                state=true;
                break;
                case "F":
                     currentTemp.setText(Integer.toString(ctof)+"°F");
                    lowTemp.setText(Integer.toString(ctof2)+"°F");
                    state=false;

                break;
            }

            cityCountry.setText(report.getCityName()+", "+ report.getCountry());
            Log.v("weatherstate",report.getWeather());
            String weatherState=report.getWeather();
            String weather="";
            switch (weatherState){
                case "Rain":
                    weather=getString(R.string.rain);
                    break;
                case "Clear":
                    weather=getString(R.string.clear);
                    break;
                case "Clouds":
                    weather=getString(R.string.clouds);
                    break;
                case "Snow":
                    weather=getString(R.string.snow);
                    break;
                case "Wind":
                    weather=getString(R.string.wind);
                    break;
                default:
                    weather=weatherState;


            }
            weatherDescription.setText(weather);


            currentTemp.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(state == true){
                        currentTemp.setText(Integer.toString(ctof)+"°F");
                        lowTemp.setText(Integer.toString(ctof2)+"°F");
                        mAdapter.notifyDataSetChanged();
                        state=false;
                        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("STATE", "F");
                        editor.apply();

                    }else if(state == false){
                        currentTemp.setText(Integer.toString(ftoc)+"°C");
                        lowTemp.setText(Integer.toString(ftoc2)+"°C");
                        mAdapter.notifyDataSetChanged();
                        state =true;
                        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("STATE", "C");
                        editor.apply();

                    }
                }
            });

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
LocationGl=location;

        if (GPSenabled.equals("TRUE")){

            SharedPreferences.Editor editor = getSharedPreferences(myPref,getApplicationContext().MODE_PRIVATE).edit();
             editor.putString("LOCATION_LAT", String.valueOf(location.getLatitude()));
            editor.putString("LOCATION_LON", String.valueOf(location.getLongitude()));
            editor.putString("LOCATION_PROVIDER", location.getProvider());
            editor.commit();
            downloadWeatherData(location);
        }else{

            SharedPreferences notif = getSharedPreferences(myPref,MODE_PRIVATE);
            String lat =notif.getString("LOCATION_LAT",String.valueOf(location.getAltitude()));
            String lon =notif.getString("LOCATION_LON",String.valueOf(location.getLongitude()));
            String provider = notif.getString("LOCATION_PROVIDER", location.getProvider());

            location1 = new Location(provider);
            location1.setLatitude(Double.parseDouble(lat));
            location1.setLongitude(Double.parseDouble(lon));
            downloadWeatherData(location1);

        }
    }

    public void downloadWeatherData(Location location){

        final String fullCoords= URL_COORD +location.getLatitude() + "&lon=" + location.getLongitude();
        //final String fullCoords=URL_COORD +71.2080+ "&lon=" +46.8139 ; // for testing purposes
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
                    Log.v("listsize",Integer.toString(list.length()));
                    weatherReportList.clear();

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
                    Gson gson = new Gson();
                    String json = gson.toJson(weatherReportList);
                    SharedPreferences.Editor editor =getSharedPreferences(myPref,MODE_PRIVATE).edit();
                    editor.putString("WeatherArray",json);
                    editor.commit();

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








    public void startLocationServices(){
        Log.v("Debugging","startedlocationservices started");
        try{
            LocationRequest req = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

    @Override
    public void onClick(View v) {
       if(v == resideMenuSetting){
            Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
       }else if(v == resideMenuHome){
                    resideMenu.closeMenu();
        }else {
           Intent intent = new Intent(getApplicationContext(),InfoActivity.class);
           startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
       }



    }


    public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder>{

        private ArrayList<DailyWeatherReport> mDailyWeatherReport;


        public WeatherAdapter(ArrayList<DailyWeatherReport> mDailyWeatherReport) {
            this.mDailyWeatherReport = mDailyWeatherReport;
         }


        @Override
        public void onBindViewHolder(final WeatherViewHolder holder, final int position) {
            final DailyWeatherReport report =mDailyWeatherReport.get(position);
            holder.updateUi(report);
        }

        @Override
        public long getItemId(int position) {
              return super.getItemId(position);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            Log.v("sizeofadapter",String.valueOf(mDailyWeatherReport.size()));
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


        public void updateUi(final DailyWeatherReport report){
            Date currentTime = Calendar.getInstance().getTime();
            Calendar c= GregorianCalendar.getInstance();

               String today= (currentTime.toString()).substring(8,10);
               String apitoday=report.getRawdate().substring(8,10);//report.getFormattedDate().substring(9,11);
               Log.v("debugtime",apitoday+"  "+report.getRawdate().substring(8,10));
            String timeOfDay =(report.getRawdate()).substring(11,13);
            String day=(report.getRawdate()).substring(8,10);
            String month=(report.getRawdate()).substring(5,7);
            String year=(report.getRawdate()).substring(0,4);
           // c.set(Integer.valueOf(year),Integer.valueOf(month),Integer.valueOf(day));
            c.set(Integer.valueOf(year),(Integer.valueOf(month))-1,Integer.valueOf(day));
            int dayofweek =c.get(Calendar.DAY_OF_WEEK);
            Log.v("currenttime",year+" "+month+" "+day+"   "+Calendar.MONDAY+ " "+String.valueOf(dayofweek));

                int actualtime =Integer.valueOf(today);
                int apitime=Integer.valueOf(apitoday);

                if( actualtime == apitime){
                    weatherDate.setText(getString(R.string.today)+timeOfDay+"h");

                }else if( apitime==actualtime+1){
                    weatherDate.setText(getString(R.string.tomorrow)+timeOfDay+"h");
                }else{
                    switch (dayofweek){
                        case(Calendar.MONDAY):
                            weatherDate.setText(getString(R.string.monday)+timeOfDay+"h");
                            break;
                        case(Calendar.TUESDAY):
                            weatherDate.setText(getString(R.string.tuesday)+timeOfDay+"h");
                            break;
                        case(Calendar.WEDNESDAY):
                            weatherDate.setText(getString(R.string.wednesday)+timeOfDay+"h");
                            break;
                        case(Calendar.THURSDAY):
                            weatherDate.setText(getString(R.string.thursday)+timeOfDay+"h");
                            break;
                        case(Calendar.FRIDAY):
                            weatherDate.setText(getString(R.string.friday)+timeOfDay+"h");
                            break;
                        case(Calendar.SATURDAY):
                            weatherDate.setText(getString(R.string.saturday)+timeOfDay+"h");
                            break;
                        case(Calendar.SUNDAY):
                            weatherDate.setText(getString(R.string.sunday)+timeOfDay+"h");
                            break;
                    }
                }

            String weatherStat=report.getWeather();
            String weathe="";
            switch (weatherStat){
                case "Rain":
                    weathe=getString(R.string.rain);
                    break;
                case "Clear":
                    weathe=getString(R.string.clear);
                    break;
                case "Clouds":
                    weathe=getString(R.string.clouds);
                    break;
                case "Snow":
                    weathe=getString(R.string.snow);
                    break;
                case "Wind":
                    weathe=getString(R.string.wind);
                    break;
                default:
                    weathe=weatherStat;


            }





            weatherDescription.setText(weathe);

            if(state == false){

                int val1=((report.getMaxTemp())*9/5)+32;
                int val2=((report.getMinTemp())*9/5)+32;
                tempHigh.setText(String.valueOf(val1)+"°F");
                tempLow.setText(String.valueOf(val2)+"°F");
            }else if(state == true){
                tempHigh.setText(Integer.toString(report.getMaxTemp())+"°C");
                tempLow.setText(Integer.toString(report.getMinTemp())+"°C");

            }











            switch (report.getWeather()){
                case DailyWeatherReport.WEATHER_TYPE_CLOUDS:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.cloud_min));
                    break;
                case DailyWeatherReport.WEATHER_TYPE_RAIN:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.rain_min));
                    break;
                case DailyWeatherReport.WEATHER_TYPE_SNOW:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.snow_min));
                    break;
                case DailyWeatherReport.WEATHER_TYPE_WIND:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.thunder_min));
                    break;
                default:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.sun_min));
            }
        }
    }

}











