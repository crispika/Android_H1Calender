    package com.comp90018.H1Calendar;
    import android.Manifest;
    import android.app.Activity;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.os.Bundle;
    import android.widget.Toast;

    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.ActivityCompat;
    import androidx.core.content.ContextCompat;

    import com.comp90018.H1Calendar.utils.CalenderEvent;
    import com.google.gson.Gson;
    import com.google.zxing.integration.android.IntentIntegrator;
    import com.google.zxing.integration.android.IntentResult;



    public class AddQRCodeScheduleActivity extends AppCompatActivity {


        public static final int PHOTO_REQUEST_CAMERA = 0x0000c0de;// QR scan intent code
        public static final int CAMARA_PERMISSION_CODE = 111;




        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            int checkCamaraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if(checkCamaraPermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMARA_PERMISSION_CODE);
            }else {
                toCamera();
            }



        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[],int[] grantResults) {
            switch (requestCode) {
                case CAMARA_PERMISSION_CODE: {
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        toCamera();
                    }
                }
            }
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode,resultCode,data);
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:
                    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (result != null) {
                        if (result.getContents() == null) {
                            Toast.makeText(this, "cancel scan", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
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
                default:
                    break;

            }
        }



        private void toCamera() {
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


    }


