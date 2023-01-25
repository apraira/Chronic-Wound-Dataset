package com.example.chronicwound.gallery;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.chronicwound.LoginActivity;
import com.example.chronicwound.MainActivity;
import com.example.chronicwound.R;
import com.example.chronicwound.SaveSharedPreference;
import com.example.chronicwound.anotasi.anotasiTepi;
import com.example.chronicwound.anotasi.anotasiWarna;
import com.example.chronicwound.pasien.detailPasienActivity;
import com.example.chronicwound.remote.LoginResponse;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chronicwound.MainActivity.id_nurse;
import static com.example.chronicwound.logging.LogHelper.InsertLog;

public class singleImageView extends AppCompatActivity {

    TextView nama_pasien, nama_perawat, filename, created_at, category;
    ImageView imageHolder;
    private String KEY_NAME = "NRM"; //nomor registrasi pasien
    String IDPasien;
    CardView arsirWarnaLuka;
    String url_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        InsertLog(id_nurse, "Memasuki halaman single image view");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image_view_layout);

        nama_pasien = (TextView) findViewById(R.id.textNamaPasien);
        nama_perawat = (TextView) findViewById(R.id.textNamaPerawat);
        filename = (TextView) findViewById(R.id.textFileName);
        created_at = (TextView) findViewById(R.id.textCreatedAt);
        category = (TextView) findViewById(R.id.textCategory);
        imageHolder = (ImageView) findViewById(R.id.imageHolder);
        arsirWarnaLuka = (CardView) findViewById(R.id.arsirWarnaLuka);

        Bundle extras = getIntent().getExtras();
        String IDImage = extras.getString(KEY_NAME);
        String tipe = extras.getString("type");
        IDPasien = extras.getString("IDPasien");



        imageDetails(IDImage);



        Button delete = findViewById(R.id.deleteImage);

        if(tipe.equals("jpg")){
            delete.setVisibility(View.GONE);
        } else { delete.setVisibility(View.VISIBLE);}

        delete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
               deleteImage(IDImage);
            }
        });

        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleImageView.super.onBackPressed();
            }
        });

        arsirWarnaLuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), anotasiWarna.class);
                intent.putExtra("picture", url_image);
                intent.putExtra("dari", "single");
                startActivity(intent);
            }
        });






    }

    @Override
    public void onBackPressed() {
        finish();
    }

    // delete image
    public void deleteImage(final String id) {
        Call<GalleryResponse> galleryResponseCall = RetrofitClient.getService().delete_image(id);
        galleryResponseCall.enqueue(new Callback<GalleryResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {

                if (response.isSuccessful()) {
                    finish();
                } else {
                    Toast.makeText(singleImageView.this, "gagal menghapus foto", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GalleryResponse> call, Throwable t) {
                Toast.makeText(singleImageView.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });}

    // image details
    public void imageDetails(final String id) {
        Call<GalleryResponse> imageResponseCall = RetrofitClient.getService().getImageDetail(id);
        imageResponseCall.enqueue(new Callback<GalleryResponse>() {
            @Override
            public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {

                if (response.isSuccessful()) {

                    Glide.with(getApplicationContext()).load("https://jft.web.id/woundapi/instance/uploads/" + response.body().getFilename())
                            .centerCrop()
                            .thumbnail(0.05f)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(imageHolder);

                    //Picasso.get().load("https://jft.web.id/woundapi/instance/uploads/" + response.body().getFilename()).rotate(90).into(imageHolder);
                    nama_pasien.setText(response.body().getId_pasien());
                    nama_perawat.setText(response.body().getId_perawat());
                    filename.setText(response.body().getFilename());
                    created_at.setText(response.body().getCreated_at());
                    category.setText(response.body().getCategory());

                    url_image = "https://jft.web.id/woundapi/instance/uploads/" + response.body().getFilename();





                } else {
                    Toast.makeText(singleImageView.this, "gagal", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GalleryResponse> call, Throwable t) {
                Toast.makeText(singleImageView.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
}}