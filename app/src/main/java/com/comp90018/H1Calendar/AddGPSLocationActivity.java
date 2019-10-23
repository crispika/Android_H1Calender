package com.comp90018.H1Calendar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.utils.EventLocation;
import com.comp90018.H1Calendar.utils.LocationListAdapter;

import java.util.List;

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
    // variable used to store user info that get from shared preferences
    private String userId;
    protected List<EventLocation> locationList;
    private sqliteHelper db;
    private static final int LOCATION_CODE = 1;

    @BindView(R.id.gps_location_input)
    EditText gps_location_input;

    @BindView(R.id.gps_latlng)
    TextView gps_latlng;

    @BindView(R.id.edit_location_list)
    ListView locationListView;

    @OnClick(R.id.gps_save)
    void addLocation(){
        locationName = gps_location_input.getText().toString();
        if (curLocation != null && !locationName.equals("")){
            //TODO: save location with name to database
            sqliteHelper db = new sqliteHelper(this);
            EventLocation eventLocation = new EventLocation(userId,locationName,getlatlng());
            Boolean status = db.insertLocations(eventLocation);

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
        db = new sqliteHelper(this);
        loadUserInfo();
        //Todo: loading saved locationns
        locationList = db.getAllLocationsByUserId(userId);
        LocationListAdapter locationListAdapter = new LocationListAdapter(this,locationList);
        locationListView.setAdapter(locationListAdapter);
        locationListView.setOnItemClickListener(new OnClickLocationListner());


        //Todo: getting current location


        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
        }else {
            grantLocation();
        }




    }

    private void grantLocation(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List list = locationManager.getProviders(true);

        String provider = "";

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;
        }
        if((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) ){
            locationManager.requestLocationUpdates(provider, 500, 10, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    grantLocation();
                }
            }
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

    }

    private class OnClickLocationListner implements AdapterView.OnItemClickListener, PopupMenu.OnMenuItemClickListener {

        protected String selectedLocationID;

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            EventLocation el = (EventLocation)adapterView.getItemAtPosition(i);
            selectedLocationID = el.getLocationId();
            PopupMenu popup = new PopupMenu(getApplicationContext(),view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.popup, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // TODO Auto-generated method stub
            switch (item.getItemId()) {
                case R.id.delete_location:
                    Toast.makeText(getApplicationContext(), "delete location", Toast.LENGTH_SHORT).show();
                    db.deleteLocationByLocationId(selectedLocationID);
                    locationList = db.getAllLocationsByUserId(userId);
                    LocationListAdapter locationListAdapter = new LocationListAdapter(getApplicationContext(),locationList);
                    locationListView.setAdapter(locationListAdapter);
                    break;
                case R.id.cancel_delete:
                    break;
                default:
                    break;
            }
            return false;
        }


    }

}
