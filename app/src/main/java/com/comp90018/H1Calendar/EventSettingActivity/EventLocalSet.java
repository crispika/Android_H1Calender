package com.comp90018.H1Calendar.EventSettingActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.R;
import com.comp90018.H1Calendar.utils.EventLocation;
import com.comp90018.H1Calendar.utils.LocationListAdapter;

import java.util.List;

import butterknife.BindView;

import butterknife.OnClick;
import butterknife.ButterKnife;

public class EventLocalSet extends Activity {
    // store user info into shared preferences
    private static final String SHAREDPREFS  = "sharedPrefs";
    private static final String USERID = "userid";
    // variable used to store user info that get from shared preferences
    private String userId;


    private LocationListAdapter locationListAdapter;
    private String coordinate;

    @BindView(R.id.location_input)
    EditText location_input;

    @BindView(R.id.location_list)
    ListView location_list;

    @OnClick(R.id.location_cancel)
    void cancelLocal() {
        finish();
    }

    @OnClick(R.id.location_save)
    void saveLocal(){
        String location = location_input.getText().toString();
        if(!location.equals("")){
            Intent intent= new Intent();
//            intent.putExtra("hasCoor",false);
            intent.putExtra("location", location);
            setResult(1, intent);
            finish();
        }else {
            Toast.makeText(getApplicationContext(), "Location Missing",
                    Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        ButterKnife.bind(this);
        sqliteHelper db = new sqliteHelper(this);
        //TODO: getting saved location list from DB
        //test
        List<EventLocation> loclist = db.getAllLocationsByUserId(getUserID());
        locationListAdapter = new LocationListAdapter(this,loclist);
        location_list.setAdapter(locationListAdapter);
        location_list.setOnItemClickListener(new OnClickLocationListner());


    }


   private class OnClickLocationListner implements AdapterView.OnItemClickListener{

       @Override
       public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           EventLocation el = (EventLocation)adapterView.getItemAtPosition(i);
           Intent intent= new Intent();
           intent.putExtra("has_coor",true);
           intent.putExtra("location",el.getName());
           intent.putExtra("coordinate",el.getCoordinate());
           intent.putExtra("locationID",el.getLocationId());
           setResult(1, intent);
           finish();
       }
   }

    public String getUserID(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USERID, "");

    }
}
