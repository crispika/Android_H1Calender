package com.comp90018.H1Calendar.EventSettingActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.comp90018.H1Calendar.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventLocalSet extends Activity {

    @BindView(R.id.location_input)
    EditText location_input;
    @OnClick(R.id.location_cancel)
    void cancelLocal() {
        Intent intent=new Intent();
        setResult(1, intent);
        finish();
    }

    @OnClick(R.id.location_save)
    void saveLocal(){
        String location = location_input.getText().toString();
        if(!location.equals("")){
            Intent intent= new Intent();
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

    }
}
