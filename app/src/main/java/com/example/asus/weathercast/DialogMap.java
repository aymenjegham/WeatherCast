package com.example.asus.weathercast;

 import android.content.Context;
 import android.content.SharedPreferences;
 import android.os.Bundle;
import android.support.annotation.Nullable;
 import android.support.v4.app.FragmentTransaction;
 import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 import android.widget.Toast;

 import com.google.android.gms.maps.CameraUpdateFactory;
 import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
 import com.google.android.gms.maps.model.BitmapDescriptorFactory;
 import com.google.android.gms.maps.model.LatLng;
 import com.google.android.gms.maps.model.Marker;
 import com.google.android.gms.maps.model.MarkerOptions;

 import java.util.Objects;

 import static com.example.asus.weathercast.SettingsActivity.myPref;


public class DialogMap  extends android.support.v4.app.DialogFragment implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private static  final  String TAG="MyDialogMap";
    Context context;

    GoogleMap mMap;
    public DialogMap() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        return view;

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        LatLng latLng = new LatLng(0,0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,1));

        final MarkerOptions markerOptions = new MarkerOptions();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mMap.addMarker(markerOptions);

                SharedPreferences.Editor editor = getContext().getSharedPreferences(myPref,Context.MODE_PRIVATE).edit();
                editor.putString("LOCATION_LAT", String.valueOf(latLng.latitude));
                editor.putString("LOCATION_LON", String.valueOf(latLng.longitude));
               // editor.putString("LOCATION_PROVIDER", latLng.getProvider());
                editor.commit();


            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        assert getFragmentManager() != null;
         SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ft.remove(mapFragment);
        ft.commit();
    }

}
