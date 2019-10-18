package com.comp90018.H1Calendar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddGPSLocationActivity extends AppCompatActivity implements LocationListener{
    protected LocationManager locationManager;
    protected Location curLocation;
    protected String latitude,longitude;
    private String locationName;

    @BindView(R.id.gps_location_input)
    EditText gps_location_input;

    @BindView(R.id.gps_latlng)
    TextView gps_latlng;

    @OnClick(R.id.gps_save)
    void addLocation(){
        locationName = gps_location_input.getText().toString();
        if (curLocation != null && !locationName.equals("")){
            //TODO: save location with name to database
            //insertGPS2DB(locationName, getLatlng())
            System.out.println("Location Save: "+locationName+" "+getlatlng());
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

    }

    @OnClick(R.id.gps_cancel)
    public void cancelLocalAdd() {
        System.out.println("test cancel");
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_gps_location);
        ButterKnife.bind(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        curLocation = location;
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        System.out.println("find here "+getlatlng());
        gps_latlng.setText("Current Location: \n"+ latitude +", "+longitude);


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private String getlatlng(){
        if(latitude == null){
            return " ";
        }
        return latitude+","+longitude;
    }

}
