package com.example.chronicwound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chronicwound.remote.PasienRequest;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UploadRequest;
import com.example.chronicwound.remote.UserService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class konfirmasiFotoActivity extends AppCompatActivity {

    private Uri photo;
    private String KEY_PHOTO = "PHOTO";
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_foto);
        ImageView img= (ImageView) findViewById(R.id.img_konfirmasi);

        /* Getting ImageBitmap from Camera from Main Activity */
        Intent intent_camera = getIntent();
        Uri uri = intent_camera.getParcelableExtra(KEY_PHOTO);
        File file = new File(uri.getPath());//create path from uri

        if (uri != null) {
            img.setImageURI(uri);
        }
    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        buttonSubmit = (Button) findViewById(R.id.buttonUpload);
    }


    // upload image
    public void uploadImage(final MultipartBody.Part image, final String id_pasien, final Integer id_perawat, final String category){
        Call<UploadRequest> uploadRequestCall = RetrofitClient.getService().uploadImage(image, id_pasien, id_perawat, category);
        uploadRequestCall.enqueue(new Callback<UploadRequest>() {
            @Override
            public void onResponse(Call<UploadRequest> call, Response<UploadRequest> response) {

                if(response.isSuccessful()){
                    //login start main activity
                    Snackbar.make(buttonSubmit, "Patient created successfully!", Snackbar.LENGTH_LONG).show();
                    Intent i = new Intent(konfirmasiFotoActivity.this, listPasienActivity.class);
                    startActivity(i);
                    finish();

                }else {
                    Snackbar.make(buttonSubmit, "Unable to add new patient", Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UploadRequest> call, Throwable t) {
                Toast.makeText(konfirmasiFotoActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}


