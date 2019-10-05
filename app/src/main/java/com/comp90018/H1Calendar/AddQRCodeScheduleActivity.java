    package com.comp90018.H1Calendar;
    import android.app.Activity;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.net.Uri;
    import android.os.Bundle;
    import android.os.Environment;
    import android.provider.MediaStore;
    import android.widget.ImageView;

    import androidx.annotation.Nullable;

    import java.io.File;
    import java.io.IOException;

    import butterknife.BindView;
    import butterknife.ButterKnife;
    import butterknife.OnClick;


    public class AddQRCodeScheduleActivity extends Activity {
        private static Uri imageUri;
        public static final int PHOTO_REQUEST_CAMERA = 100;// 相机
        public static final int PHOTO_REQUEST_GALLERY = 101;// 相册
        public static File tempFile;
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
                case PHOTO_REQUEST_GALLERY:   //相册返回的数据（相册的返回码）
                    Uri uri02 = data.getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri02));
                        image.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }


        private void toGallery(){
            Intent intent = new Intent(Intent.ACTION_PICK);  //跳转到 ACTION_IMAGE_CAPTURE
            intent.setType("image/*");
            startActivityForResult(intent,PHOTO_REQUEST_GALLERY); // 101: 相机的返回码参数（随便一个值就行，只要不冲突就好）
        }

        private void toCamera() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, PHOTO_REQUEST_CAMERA);
            }
        }





    }


