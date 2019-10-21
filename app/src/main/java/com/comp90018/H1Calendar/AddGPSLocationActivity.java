package com.comp90018.H1Calendar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.utils.EventLocation;
import com.comp90018.H1Calendar.utils.LocationListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddGPSLocationActivity extends AppCompatActivity implements LocationListener{
    protected LocationManager locationManager;
    protected Location curLocation;
    protected String latitude,longitude;
    private String locationName;
    // store user info into shared preferences
    private static final String SHAREDPREFS  = "sharedPrefs";
    private static final String USERID = "userid";
    private static final String USEREMAIL = "useremail";
    private static final String USERPWD = "userpwd";
    // variable used to store user info that get from shared preferences
    private String userId, userEmail, userPwd;

    @BindView(R.id.gps_location_input)
    EditText gps_location_input;

    @BindView(R.id.gps_latlng)
    TextView gps_latlng;

    @OnClick(R.id.gps_save)
    void addLocation(){
        locationName = gps_location_input.getText().toString();
        if (curLocation != null && !locationName.equals("")){
            //TODO: save location with name to database
            sqliteHelper db = new sqliteHelper(this);
            loadUserInfo();
            EventLocation location = new EventLocation(userId,locationName,getlatlng());
            Boolean status = db.insertLocations(location);

            if(status){
                Toast.makeText(this, "Location Save Successful!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Save failed!", Toast.LENGTH_SHORT).show();
            }
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
        sqliteHelper db = new sqliteHelper(this);
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

    public void loadUserInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);

        // the default values of these three variables are ""
        userId = sharedPreferences.getString(USERID, "");
        userEmail = sharedPreferences.getString(USEREMAIL, "");
        userPwd = sharedPreferences.getString(USERPWD, "");

    }

}
