package com.comp90018.H1Calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingActivity extends AppCompatActivity {
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
        nightAuto = !nightAuto;

    }

    @OnClick(R.id.sw_night_mode)
    void nightMode() {
        nightMode = !nightMode;

    }

    @OnClick(R.id.sw_shake)
    void shakeMode() {
        shakeMode = !shakeMode;
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
        System.out.println("night auto : "+nightAuto);
        sw_night_mode_auto.setChecked(nightAuto);
        sw_night_mode.setChecked(nightMode);
        sw_shake.setChecked(shakeMode);



    }
    @Override
    public void onBackPressed() {
        settingSave();
    }

    private void saveSettingPref(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("nightModeAuto",nightAuto);
        editor.putBoolean("nightMode",nightMode);
        editor.putBoolean("shakeMode",shakeMode);
        editor.apply();

    }

    public void loadSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);

        // the default values of these setting variables are ""
        nightAuto = sharedPreferences.getBoolean("nightModeAuto",false);
        nightMode = sharedPreferences.getBoolean("nightMode",false);
        shakeMode = sharedPreferences.getBoolean("shakeMode",false);

    }


}
