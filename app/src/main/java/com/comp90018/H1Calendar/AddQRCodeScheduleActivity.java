    package com.comp90018.H1Calendar;
    import android.app.Activity;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.net.Uri;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.widget.ImageView;

    import androidx.annotation.Nullable;

    import java.io.IOException;

    import butterknife.BindView;
    import butterknife.ButterKnife;
    import butterknife.OnClick;


    public class AddQRCodeScheduleActivity extends Activity {
        public static final int PHOTO_REQUEST_CAMERA = 100;// camera intent code
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


        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_qrcode_schedule);
            ButterKnife.bind(this);

        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:
                    if (resultCode == RESULT_OK) {
                        Bundle extras = data.getExtras();
                        Bitmap bitmap = (Bitmap) extras.get("data");
                        image.setImageBitmap(bitmap);
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
            }
        }


        private void toGallery(){
            Intent intent = new Intent(Intent.ACTION_PICK);  // open gallery
            intent.setType("image/*");
            startActivityForResult(intent,PHOTO_REQUEST_GALLERY);
        }

        private void toCamera() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, PHOTO_REQUEST_CAMERA);
            }
        }





    }


