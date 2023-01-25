package com.example.chronicwound.pasien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.R;
import com.example.chronicwound.editProfilPerawat;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class editPasien extends AppCompatActivity {

    TextView textNoReg, textNama, textTanggalLahir, textUsia, textJenisKelamin;
    TextView textAgama, textEmail, textNoHp, textAlamat;

    String NRM;

    Button fotoPasien;

    ImageButton editNoReg, editNama, editTanggalLahir,editUsia, editJenisKelamin;
    ImageButton editAgama, editEmail, editNoHp, editAlamat;

    public void initViews(){
        textNoReg = (TextView) findViewById(R.id.textNoReg);
        textNama = (TextView) findViewById(R.id.textNama);
        textTanggalLahir = (TextView) findViewById(R.id.textTanggalLahir);
        textUsia = (TextView) findViewById(R.id.textUsia);
        textJenisKelamin = (TextView) findViewById(R.id.textJenisKelamin);
        textEmail = (TextView) findViewById(R.id.textEmail);
        textNoHp = (TextView) findViewById(R.id.textNoHp);
        textAgama = (TextView) findViewById(R.id.textAgama);
        textAlamat = (TextView) findViewById(R.id.textAlamat);

        //btn edit
        editNoReg = (ImageButton) findViewById(R.id.editNoReg);
        editNama = (ImageButton) findViewById(R.id.editNama);
        editTanggalLahir = (ImageButton) findViewById(R.id.editTanggalLahir);
        editUsia = (ImageButton) findViewById(R.id.editUsia);
        editJenisKelamin = (ImageButton) findViewById(R.id.editJenisKelamin);
        editAgama = (ImageButton) findViewById(R.id.editAgama);
        editEmail = (ImageButton) findViewById(R.id.editEmail);
        editNoHp = (ImageButton) findViewById(R.id.editNoHp);
        editAlamat = (ImageButton) findViewById(R.id.editAlamat);

        //button tambah foto
        fotoPasien = (Button) findViewById(R.id.fotoPasien);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pasien);

        initViews();


        //receives data
        Bundle extras = getIntent().getExtras();
        NRM = extras.getString("NRM");

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("NRM", NRM);
        editor.commit();




        cariPasien(NRM);

        // fungsi edit
        editNoReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editProfilPerawat.class);
                intent.putExtra("siapa", "pasien");
                intent.putExtra("edit", "no. reg");
                intent.putExtra("text", textNoReg.getText());
                startActivity(intent);
                finish();
            }
        });

        editNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editProfilPerawat.class);
                intent.putExtra("siapa", "pasien");
                intent.putExtra("edit", "nama");
                intent.putExtra("text", textNama.getText());
                startActivity(intent);
                finish();
            }
        });

        editTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editProfilPerawat.class);
                intent.putExtra("siapa", "pasien");
                intent.putExtra("edit", "tanggal lahir");
                intent.putExtra("text", textTanggalLahir.getText());
                startActivity(intent);
                finish();
            }
        });

        editUsia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editProfilPerawat.class);
                intent.putExtra("siapa", "pasien");
                intent.putExtra("edit", "usia");
                intent.putExtra("text", textUsia.getText());
                startActivity(intent);
                finish();
            }
        });

        editJenisKelamin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editProfilPerawat.class);
                intent.putExtra("siapa", "pasien");
                intent.putExtra("edit", "jenis kelamin");
                intent.putExtra("text", textJenisKelamin.getText());
                startActivity(intent);
                finish();
            }
        });

        editAgama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editProfilPerawat.class);
                intent.putExtra("siapa", "pasien");
                intent.putExtra("edit", "agama");
                intent.putExtra("text", textAgama.getText());
                startActivity(intent);
                finish();
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editProfilPerawat.class);
                intent.putExtra("siapa", "pasien");
                intent.putExtra("edit", "email");
                intent.putExtra("text", textEmail.getText());
                startActivity(intent);
                finish();
            }
        });

        editNoHp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editProfilPerawat.class);
                intent.putExtra("siapa", "pasien");
                intent.putExtra("edit", "nomor hp");
                intent.putExtra("text", textNoHp.getText());
                startActivity(intent);
                finish();
            }
        });

        editAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editProfilPerawat.class);
                intent.putExtra("siapa", "pasien");
                intent.putExtra("edit", "alamat");
                intent.putExtra("text", textAlamat.getText());
                startActivity(intent);
                finish();
            }
        });


        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), detailPasienActivity.class);

                Bundle extras = getIntent().getExtras();
                String NRM = extras.getString("NRM");
                i.putExtra("NRM", NRM);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), detailPasienActivity.class);
        Bundle extras = getIntent().getExtras();
        String NRM = extras.getString("NRM");
        i.putExtra("NRM", NRM);
        startActivity(i);
        finish();
    }

    // cari pasien
    public void cariPasien(final String nrm) {
        Call<PasienResponse> pasienResponseCall = RetrofitClient.getService().cariPasienNRM(nrm);
        pasienResponseCall.enqueue(new Callback<PasienResponse>() {
            @Override
            public void onResponse(Call<PasienResponse> call, Response<PasienResponse> response) {

                if (response.isSuccessful()) {
                    //login start main activity
                    textNoReg.setText(response.body().get_id());
                    textNama.setText(response.body().getNama());
                    textTanggalLahir.setText(response.body().getBorn_date());
                    textUsia.setText(response.body().getUsia() + " Tahun");
                    textJenisKelamin.setText(response.body().getKelamin());
                    textAgama.setText(response.body().getAgama());
                    textEmail.setText(response.body().getEmail());
                    textNoHp.setText(response.body().getNo_hp());
                    textAlamat.setText(response.body().getAlamat());



                } else {
                    Toast.makeText(getApplicationContext(), "gagal mendapatkan data pasien", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<PasienResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}