package com.cl.slack.cameraforresult;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class CapturePictureActivity extends AppCompatActivity {

    private static final String TAG = "CapturePictureActivity";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0x100;
    private static final int CHECK_PERMISSION_REQUEST_CODE = 0x101;
    private Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate..." + this.toString());
        setContentView(R.layout.activity_camera_capture);
        initSaveUri();
        checkPermission(Manifest.permission.CAMERA);
        // 自定义拍照 使用的google demo
        getFragmentManager().beginTransaction()
                .replace(R.id.container, Camera2BasicFragment.newInstance().setBitmapListener(listener))
                .commit();
    }

    /**
     * this is only one param
     *
     * @param permissions
     */
    private void checkPermission(String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openSystemCamera();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            openSystemCamera();
        } else {
            ActivityCompat.requestPermissions(this,
                    Arrays.copyOf(permissions, 1), CHECK_PERMISSION_REQUEST_CODE);
        }
    }

    private void initSaveUri() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle b = intent.getExtras();
            if (b != null) {
                saveUri = b.getParcelable(MediaStore.EXTRA_OUTPUT);
            } else {
                saveUri = null;
            }
        } else {
            saveUri = null;
        }
    }

    /**
     * 简单的演示，就直接利用系统自带的相机应用:拍照
     */
    private void openSystemCamera() {
        Log.i(TAG, "openSystemCamera...");
        // 调用系统拍照
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        // 测试直接返回一张图片
//        testBitmap();

    }

    Camera2BasicFragment.OnPhotoListener listener = new Camera2BasicFragment.OnPhotoListener() {
        @Override
        public void onBitmap(Bitmap bitmap) {
            returnBackBitmap(bitmap);
        }

        @Override
        public void onByte(byte[] bytes) {
            returnBackBitmap(bytes);
        }
    };

    private void testBitmap(){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getAssets().open("testImg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnBackBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (data == null) {
                finish();
                return;
            }

            Bitmap photo = data.getParcelableExtra("data");

            if (photo == null) {
                finish();
                return;
            }

            returnBackBitmap(photo);

        }
    }

    private void returnBackBitmap(Bitmap photo) {
        if (saveUri != null) {
            // Save the bitmap to the specified URI (use a try/catch block)
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(saveUri);
                if (outputStream != null) {
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);// write your bitmap here
//                        setResult(RESULT_OK);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                photo.recycle();
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }

        } else {
            finish();
        }
    }

    private void returnBackBitmap(byte[] bytes) {
        if (saveUri != null) {
            // Save the bitmap to the specified URI (use a try/catch block)
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(saveUri);
                if (outputStream != null) {
                    outputStream.write(bytes);// write your bitmap here
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }

        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CHECK_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openSystemCamera();
            }
        }
    }

    @Override
    public void finish() {
        Log.i(TAG, "finish...");
        setResult(RESULT_OK);
        super.finish();
    }
}
