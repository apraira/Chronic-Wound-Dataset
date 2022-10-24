package com.example.chronicwound.tambahkajian;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.chronicwound.LoginActivity;
import com.example.chronicwound.MainActivity;
import com.example.chronicwound.konfirmasiFotoActivity;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UploadRequest;
import com.example.chronicwound.remote.UserService;
import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chronicwound.R;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tambahKajianActivity extends AppCompatActivity {

    //variabel kamera
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap photo;
    private String KEY_PHOTO = "PHOTO";
    private String KEY_URI = "URI";
    UserService userService;
    private File FilePath;
    private String imagePath;
    Uri image, uri;
    String mCameraFileName;
    private String KEY_NAME = "NRM";
    //Dropdown
    AutoCompleteTextView opsiSize, opsiEdges, opsiNType, opsiNAmount, opsiSkinColor, opsiGranulation, opsiEpithelization;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_kajian_foto);
        MaterialCardView fab = findViewById(R.id.cameraButton);
        LinearLayout layoutImage = findViewById(R.id.layoutImageRaw);
        ImageView RawImageView = findViewById(R.id.rawImageView);


        Bundle extras = getIntent().getExtras();
        String NRM = extras.getString(KEY_NAME);
        Button submit = findViewById(R.id.buttonNext);

        Intent intent_camera = getIntent();
        Uri uri = intent_camera.getParcelableExtra("URI");

        if (uri != null) {
            fab.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
            layoutImage.setVisibility(View.VISIBLE);
            RawImageView.setImageURI(uri);
        }else {
            fab.setVisibility(View.VISIBLE);
            layoutImage.setVisibility(View.GONE);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                cameraIntent();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), tambahKajianSize.class);
                i.putExtra(KEY_URI, uri);
                startActivity(i);
            }
        });



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void cameraIntent() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("-mm-ss");

        String newPicFile = df.format(date) + ".jpg";
        String outPath = "/sdcard/" + newPicFile;
        File outFile = new File(outPath);

        mCameraFileName = outFile.toString();
        Uri outuri = Uri.fromFile(outFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                if (data != null) {
                    image = data.getData();

                    Intent IntentCamera = new Intent(tambahKajianActivity.this, konfirmasiFotoActivity.class);
                    IntentCamera.putExtra(KEY_PHOTO, image);
                    startActivity(IntentCamera);
                }
                if (image == null && mCameraFileName != null) {
                    File file = new File(mCameraFileName);

                    /** upload image below line **/
                    Bundle extras = getIntent().getExtras();
                    String NRM = extras.getString(KEY_NAME);
                    String id_pasien = NRM;
                    Integer id_perawat = 820001;
                    String category = "Raw";
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                    RequestBody pasien_id = RequestBody.create(MediaType.parse("multipart/form-data"),id_pasien);
                    RequestBody perawat_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id_perawat));
                    RequestBody kategori = RequestBody.create(MediaType.parse("multipart/form-data"), category);;
                    uploadImage(body, pasien_id, perawat_id, kategori);

                    image = Uri.fromFile(new File(mCameraFileName));
                    Intent IntentCamera = new Intent(tambahKajianActivity.this, konfirmasiFotoActivity.class);
                    IntentCamera.putExtra(KEY_PHOTO, image);
                    startActivity(IntentCamera);
                }
                File file = new File(mCameraFileName);
                if (!file.exists()) {
                    file.mkdir();
                }
            }
        }
    }

    // upload image
    public void uploadImage(final MultipartBody.Part image, final RequestBody id_pasien, final RequestBody id_perawat, final RequestBody category){
        Call<UploadRequest> uploadRequestCall = RetrofitClient.getService().uploadImage(image, id_pasien, id_perawat, category);
        uploadRequestCall.enqueue(new Callback<UploadRequest>() {
            @Override
            public void onResponse(Call<UploadRequest> call, Response<UploadRequest> response) {

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Image uploaded to server", Toast.LENGTH_LONG).show();
                    finish();

                }else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UploadRequest> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

}