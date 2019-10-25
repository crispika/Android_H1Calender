package com.comp90018.H1Calendar.EventSettingActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.EventDetailActivity;
import com.comp90018.H1Calendar.R;
import com.comp90018.H1Calendar.utils.CalenderEvent;
import com.comp90018.H1Calendar.utils.QRShareUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventQRShare extends AppCompatActivity {

    private String event_id;

    @BindView(R.id.qr_image)
    ImageView qrImage;

    @OnClick(R.id.qr_back)
    void back() {
        Intent intent = new Intent(this, EventDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", event_id);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_event_share);
        ButterKnife.bind(this);
        event_id = getIntent().getStringExtra("event_id");
        CalenderEvent event = (CalenderEvent) new sqliteHelper(getApplicationContext()).getEventByEventId(event_id);
        String jsonContent = event.toJsonStr();
        Bitmap qrcode = QRShareUtils.createQRCodeBitmap(jsonContent, 700, 700);

        qrImage.setImageBitmap(qrcode);
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, EventDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", event_id);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
