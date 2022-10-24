package com.example.chronicwound.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.R;
import com.example.chronicwound.pasien.detailPasienActivity;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class singleImageView extends AppCompatActivity {

    TextView nama_pasien, nama_perawat, filename, created_at, category;
    ImageView imageHolder;
    private String KEY_NAME = "NRM"; //nomor registrasi pasien

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image_view_layout);

        nama_pasien = (TextView) findViewById(R.id.textNamaPasien);
        nama_perawat = (TextView) findViewById(R.id.textNamaPerawat);
        filename = (TextView) findViewById(R.id.textFileName);
        created_at = (TextView) findViewById(R.id.textCreatedAt);
        category = (TextView) findViewById(R.id.textCategory);
        imageHolder = (ImageView) findViewById(R.id.imageHolder);

        Bundle extras = getIntent().getExtras();
        String IDImage = extras.getString(KEY_NAME);

        imageDetails(IDImage);



    }

    // cari pasien
    public void imageDetails(final String id) {
        Call<GalleryResponse> imageResponseCall = RetrofitClient.getService().getImageDetail(id);
        imageResponseCall.enqueue(new Callback<GalleryResponse>() {
            @Override
            public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {

                if (response.isSuccessful()) {

                    Picasso.get().load("https://jft.web.id/woundapi/instance/uploads/" + response.body().getFilename()).rotate(90).into(imageHolder);
                    nama_pasien.setText(response.body().getId_pasien());
                    nama_perawat.setText(response.body().getId_perawat());
                    filename.setText(response.body().getFilename());
                    created_at.setText(response.body().getCreated_at());
                    category.setText(response.body().getCategory());





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