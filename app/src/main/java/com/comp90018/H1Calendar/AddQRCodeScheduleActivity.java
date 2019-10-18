    package com.comp90018.H1Calendar;
    import android.app.Activity;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.net.Uri;
    import android.os.Bundle;
    import android.widget.ImageView;
    import android.widget.Toast;

    import androidx.annotation.Nullable;
    import java.io.IOException;

    import butterknife.BindView;
    import butterknife.ButterKnife;
    import butterknife.OnClick;

    import com.comp90018.H1Calendar.utils.CalenderEvent;
    import com.comp90018.H1Calendar.utils.ShakeUtils;
    import com.google.gson.Gson;
    import com.google.zxing.integration.android.IntentIntegrator;
    import com.google.zxing.integration.android.IntentResult;


    public class AddQRCodeScheduleActivity extends Activity {

        public static final int PHOTO_REQUEST_CAMERA = 0x0000c0de;// QR scan intent code
        public static final int PHOTO_REQUEST_GALLERY = 101;// gallery intent code
        @BindView(R.id.QR_photo) ImageView image;
        @OnClick(R.id.bottom_camera)
        void openCamera(){
            toCamera();
        }
        @OnClick(R.id.bottom_gallery)
        void openGallery(){
            toGallery();
        }

        private ShakeUtils shakeItOff;


        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_qrcode_schedule);
            ButterKnife.bind(this);
            initAccelormeter();

        }

        @Override
        protected void onResume() {
            super.onResume();
            shakeItOff.register();
        }

        @Override
        protected void onPause() {
            super.onPause();
            shakeItOff.unRegister();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:
                    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (result != null) {
                        if (result.getContents() == null) {
                            Toast.makeText(this, "cancel scan", Toast.LENGTH_LONG).show();
                        } else {
                            CalenderEvent cEvent = QRdata2Event(result.getContents());
                            Intent intent_to_form = new Intent();
                            intent_to_form.setClass(this, AddFormScheduleActivity.class);
                            intent_to_form.putExtra("type","addFromQR");
                            intent_to_form.putExtra("QR_event",cEvent);
                            System.out.println(cEvent.toJsonStr());
                            startActivity(intent_to_form);
                        }
                    }
                    break;
                case PHOTO_REQUEST_GALLERY:
                    Uri uri = data.getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        image.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;

            }
        }


        private void toGallery(){
            Intent intent = new Intent(Intent.ACTION_PICK);  // open gallery
            intent.setType("image/*");
            startActivityForResult(intent,PHOTO_REQUEST_GALLERY);
        }

        private void toCamera() {
//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            IntentIntegrator intentIntegrator = new IntentIntegrator(AddQRCodeScheduleActivity.this);
            intentIntegrator.initiateScan();

        }

        private CalenderEvent QRdata2Event(String dataStr){
            CalenderEvent event = new CalenderEvent();
            Gson gson = new Gson();
            try {
                event = gson.fromJson(dataStr,CalenderEvent.class);
            }catch (Exception e) {
                e.printStackTrace();
            }
            return event;
        }

        private void initAccelormeter(){
            shakeItOff = new ShakeUtils(this);
            shakeItOff.setOnShakeListener(new ShakeUtils.OnShakeListener() {
                @Override
                public void onShake() {
                    Intent intent = new Intent(AddQRCodeScheduleActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }


