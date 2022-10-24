package com.example.chronicwound;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chronicwound.remote.PasienRequest;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UploadRequest;
import com.example.chronicwound.remote.UserService;
import com.example.chronicwound.tambahkajian.tambahKajianActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class konfirmasiFotoActivity extends AppCompatActivity {

    private Uri photo;
    String path;
    private String KEY_PHOTO = "PHOTO";
    private String KEY_URI = "URI";
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_foto);
        ImageView img= (ImageView) findViewById(R.id.img_konfirmasi);

        initViews();



        /* Getting ImageBitmap from Camera from Main Activity */
        Intent intent_camera = getIntent();
        Uri uri = intent_camera.getParcelableExtra(KEY_PHOTO);;


        if (uri != null) {
            img.setImageURI(uri);
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Toast.makeText(konfirmasiFotoActivity.this, "Button Clicked", Toast.LENGTH_LONG).show();
                Intent IntentCamera = new Intent(konfirmasiFotoActivity.this, tambahKajianActivity.class);
                IntentCamera.putExtra(KEY_URI, uri);
                startActivity(IntentCamera);
            }
        });


    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        buttonSubmit = (Button) findViewById(R.id.buttonUpload);
    }



}