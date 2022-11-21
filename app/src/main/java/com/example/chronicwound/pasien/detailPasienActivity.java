package com.example.chronicwound.pasien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.R;
import com.example.chronicwound.gallery.GaleriActivity;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.tambahkajian.tambahKajianActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class detailPasienActivity extends AppCompatActivity {

    TextView nama_pasien, nomorRekamMedis, nomorHp, email, usiaPasien, tanggalLahir, jenisKelamin, Alamat;
    private String NRM, id_perawat;
    private String KEY_NAME = "NRM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_detail_pasien);

        nomorRekamMedis = (TextView) findViewById(R.id.nomorRekamMedis);
        nomorHp = (TextView) findViewById(R.id.nomorHp);
        email = (TextView) findViewById(R.id.emailPasien);
        nama_pasien = (TextView) findViewById(R.id.nama_pasien);
        usiaPasien = (TextView) findViewById(R.id.usiaPasien);
        tanggalLahir = (TextView) findViewById(R.id.tanggalLahir);
        jenisKelamin = (TextView) findViewById(R.id.jenisKelamin);
        Alamat = (TextView) findViewById(R.id.alamatPasien);

        Bundle extras = getIntent().getExtras();
        NRM = extras.getString(KEY_NAME);
        id_perawat = extras.getString("id_perawat");

        cariPasien(NRM);

        FloatingActionButton fab = findViewById(R.id.tambah_pasien);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // your handler code here
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), tambahKajianActivity.class);
                i.putExtra("id_perawat", id_perawat);
                i.putExtra(KEY_NAME, NRM);
                System.out.println("sent from DETAIL PASIEN ACTIVITY" + id_perawat+ "," + NRM );
                startActivity(i);
            }
        });


        MaterialCardView btn = findViewById(R.id.galeriButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(itemView, dataList.get(position).getNrm(), Snackbar.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), GaleriActivity.class);
                i.putExtra(KEY_NAME, NRM);
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
                    nomorRekamMedis.setText("NRM: " + response.body().get_id());
                    nomorHp.setText(response.body().getNo_hp());
                    usiaPasien.setText(response.body().getUsia() + " Tahun");
                    tanggalLahir.setText(response.body().getBorn_date());
                    jenisKelamin.setText(response.body().getKelamin());
                    email.setText(response.body().getEmail());
                    Alamat.setText(response.body().getAlamat());


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