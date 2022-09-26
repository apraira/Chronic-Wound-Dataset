package com.example.chronicwound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class konfirmasiFotoActivity extends AppCompatActivity {

    private Uri photo;
    private String KEY_PHOTO = "PHOTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_foto);
        ImageView img= (ImageView) findViewById(R.id.img_konfirmasi);

        /* Getting ImageBitmap from Camera from Main Activity */
        Intent intent_camera = getIntent();
        Uri uri = intent_camera.getParcelableExtra(KEY_PHOTO);

        if (uri != null) {
            img.setImageURI(uri);
        }
    }
}