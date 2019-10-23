package com.comp90018.H1Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingActivity extends Activity {
    private static final String SHAREDPREFS = "sharedPrefs";

    boolean nightAuto,nightMode,shakeMode;
    @BindView(R.id.sw_night_mode)
    Switch sw_night_mode;
    @BindView(R.id.sw_night_mode_auto)
    Switch sw_night_mode_auto;
    @BindView(R.id.sw_shake)
    Switch sw_shake;

    @OnClick(R.id.sw_night_mode_auto)
    void nightModeAuto() {


    }

    @OnClick(R.id.sw_night_mode)
    void nightMode() {

    }

    @OnClick(R.id.sw_shake)
    void shakeMode() {

    }

    @OnClick(R.id.setting_save)
    void settingSave(){
        saveSettingPref();
        Intent intent= new Intent();
        setResult(15, intent);
        finish();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);
        ButterKnife.bind(this);
        loadSetting();
        sw_night_mode_auto.setChecked(nightAuto);
        sw_night_mode_auto.setChecked(nightMode);
        sw_shake.setChecked(shakeMode);



    }

    private void saveSettingPref(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("nightModeAuto",sw_night_mode_auto.isChecked());
        editor.putBoolean("nightMode",sw_night_mode.isChecked());
        editor.putBoolean("shakeMode",sw_night_mode.isChecked());

    }

    public void loadSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);

        // the default values of these setting variables are ""
        nightAuto = sharedPreferences.getBoolean("nightModeAuto",true);
        nightMode = sharedPreferences.getBoolean("nightMode",true);
        shakeMode = sharedPreferences.getBoolean("shakeMode",true);

    }


}
