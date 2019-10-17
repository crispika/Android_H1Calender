package com.comp90018.H1Calendar.EventSettingActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.comp90018.H1Calendar.R;
import com.comp90018.H1Calendar.utils.CalenderEvent;
import com.comp90018.H1Calendar.utils.QRShareUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventQRShare extends Activity {
    @BindView(R.id.qr_image)
    ImageView qrImage;

    @OnClick(R.id.qr_back)
    void back(){
        finish();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_event_share);
        ButterKnife.bind(this);
        CalenderEvent event = (CalenderEvent) getIntent().getSerializableExtra("event");
        String jsonContent = event.toJsonStr();
        Bitmap qrcode = QRShareUtils.createQRCodeBitmap(jsonContent,500,500);

        qrImage.setImageBitmap(qrcode);
    }
}
