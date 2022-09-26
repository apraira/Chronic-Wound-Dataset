package com.example.chronicwound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class detailPasienActivity extends AppCompatActivity {

    TextView nama_pasien, nomorRekamMedis, usiaPasien, beratBadan, tinggiBadan;
    private String NRM;
    private String KEY_NAME = "NRM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_detail_pasien);

        nomorRekamMedis = (TextView) findViewById(R.id.nomorRekamMedis);
        nama_pasien = (TextView) findViewById(R.id.nama_pasien);
        usiaPasien = (TextView) findViewById(R.id.usiaPasien);
        beratBadan = (TextView) findViewById(R.id.beratBadan);
        tinggiBadan = (TextView) findViewById(R.id.tinggiBadan);

        Bundle extras = getIntent().getExtras();
        NRM = extras.getString(KEY_NAME);

        cariPasien(NRM);

        FloatingActionButton fab = findViewById(R.id.tambah_pasien);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // your handler code here
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), tambahKajianActivity.class);
                startActivity(i);
            }
        });
    }

    // cari pasien
    public void cariPasien(final String nrm) {
        Call<PasienResponse> pasienResponseCall = RetrofitClient.getService().cariPasienNRM(nrm);
        pasienResponseCall.enqueue(new Callback<PasienResponse>() {
            @Override
            public void onResponse(Call<PasienResponse> call, Response<PasienResponse> response) {

                if (response.isSuccessful()) {
                    //login start main activity
                    nama_pasien.setText(response.body().getNama());
                    nomorRekamMedis.setText("NRM: " + response.body().getNrm());
                    usiaPasien.setText(response.body().getUsia() + " Tahun");
                    beratBadan.setText(response.body().getBerat() + "kg");
                    tinggiBadan.setText(response.body().getTinggi() + "cm");


                } else {
                    Toast.makeText(detailPasienActivity.this, "gagal", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<PasienResponse> call, Throwable t) {
                Toast.makeText(detailPasienActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}