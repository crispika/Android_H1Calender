package com.comp90018.H1Calendar.EventSettingActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import com.comp90018.H1Calendar.R;

public class EventColorSet extends AppCompatActivity implements View.OnClickListener {
    private TextView color_red,color_green,color_yellow,color_blue;
    @OnClick(R.id.color_select_back) void finishClose(){
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_color);
        ButterKnife.bind(this);
        color_red = (TextView) findViewById(R.id.color_red);
        color_yellow = (TextView) findViewById(R.id.color_yellow);
        color_green = (TextView) findViewById(R.id.color_green);
        color_blue = (TextView) findViewById(R.id.color_blue);

        color_red.setOnClickListener(this);
        color_yellow.setOnClickListener(this);
        color_green.setOnClickListener(this);
        color_blue.setOnClickListener(this);
    }



    @Override
    public void onClick(View v){
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.color_red:
                intent.putExtra("color", "Red");
                setResult(2, intent);
                finish();
                break;
            case R.id.color_yellow:
                intent.putExtra("color", "Yellow");
                setResult(2, intent);
                finish();
                break;
            case R.id.color_green:
                intent.putExtra("color", "Green");
                setResult(2, intent);
                finish();
                break;
            case R.id.color_blue:
                intent.putExtra("color", "Blue");
                setResult(2, intent);
                finish();
                break;

        }
    }
}
