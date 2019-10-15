package com.comp90018.H1Calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class EventDetailActivity extends AppCompatActivity {

    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        tv_title = findViewById(R.id.tv_event_title_detail);
        Bundle bundle = getIntent().getExtras();
        tv_title.setText(bundle.getString("title"));
    }
}
