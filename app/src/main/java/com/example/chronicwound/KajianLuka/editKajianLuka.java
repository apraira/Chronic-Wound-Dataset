package com.example.chronicwound.KajianLuka;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.R;
import com.example.chronicwound.editProfilPerawat;
import com.example.chronicwound.remote.KajianResponse;
import com.example.chronicwound.remote.RetrofitClient;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chronicwound.MainActivity.id_nurse;
import static com.example.chronicwound.logging.LogHelper.InsertLog;

public class editKajianLuka extends AppCompatActivity {

    // id image
    String RawID, TepiID, DiameterID;

    // Teks
    TextView textLuas, textTepi, textTipeNekrotik, textJumlahNekrotik, textWarnaLuka, textGranulasi, textEpitelisasi;


    // Button
    ImageButton editLuas, editTepi, editTipeNekrotik, editJumlahNekrotik, editWarnaLuka, editGranulasi, editEpitelisasi;

    public void initViews(){

        editLuas = (ImageButton) findViewById(R.id.editLuas);
        editTepi = (ImageButton) findViewById(R.id.editTepi);
        editTipeNekrotik = (ImageButton) findViewById(R.id.editTipeNekrotik);
        editJumlahNekrotik = (ImageButton) findViewById(R.id.editJumlahNekrotik);
        editWarnaLuka = (ImageButton) findViewById(R.id.editWarnaLuka);
        editGranulasi = (ImageButton) findViewById(R.id.editGranulasi);
        editEpitelisasi = (ImageButton) findViewById(R.id.editEpitelisasi);

        textLuas = (TextView) findViewById(R.id.textLuas);
        textTepi = (TextView) findViewById(R.id.textTepi);
        textTipeNekrotik = (TextView) findViewById(R.id.textTipeNekrotik);
        textJumlahNekrotik = (TextView) findViewById(R.id.textJumlahNekrotik);
        textWarnaLuka = (TextView) findViewById(R.id.textWarnaLuka);
        textGranulasi = (TextView) findViewById(R.id.textGranulasi);
        textEpitelisasi = (TextView) findViewById(R.id.textEpitelisasi);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kajian_luka);

        initViews();


        Bundle extras = getIntent().getExtras();
        String id_kajian = extras.getString("id_kajian");
        getDetail(id_kajian);


        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertLog(id_nurse, "Menekan tombol kembali dari halaman detail kajian");
                Intent i = new Intent(getApplicationContext(), detailKajian.class);
                Bundle extras = getIntent().getExtras();
                String id_kajian = extras.getString("id_kajian");
                i.putExtra("id_kajian", id_kajian);
                startActivity(i);
                finish();
            }
        });

        // fungsi edit
        editLuas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                String id_kajian = extras.getString("id_kajian");
                Intent intent = new Intent(getApplicationContext(), editSkoringLuka.class);
                intent.putExtra("edit", "luas luka");
                intent.putExtra("id_kajian", id_kajian);
                intent.putExtra("text", textLuas.getText());
                startActivity(intent);
                finish();
            }
        });

        editTepi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                String id_kajian = extras.getString("id_kajian");
                Intent intent = new Intent(getApplicationContext(), editSkoringLuka.class);
                intent.putExtra("edit", "tepi luka");
                intent.putExtra("id_kajian", id_kajian);
                intent.putExtra("text", textTepi.getText());
                startActivity(intent);
                finish();
            }
        });

        editTipeNekrotik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                String id_kajian = extras.getString("id_kajian");
                Intent intent = new Intent(getApplicationContext(), editSkoringLuka.class);
                intent.putExtra("edit", "tipe nekrotik");
                intent.putExtra("id_kajian", id_kajian);
                intent.putExtra("text", textTipeNekrotik.getText());
                startActivity(intent);
                finish();
            }
        });

        editJumlahNekrotik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                String id_kajian = extras.getString("id_kajian");
                Intent intent = new Intent(getApplicationContext(), editSkoringLuka.class);
                intent.putExtra("edit", "jumlah nekrotik");
                intent.putExtra("id_kajian", id_kajian);
                intent.putExtra("text", textJumlahNekrotik.getText());
                startActivity(intent);
                finish();
            }
        });

        editWarnaLuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                String id_kajian = extras.getString("id_kajian");
                Intent intent = new Intent(getApplicationContext(), editSkoringLuka.class);
                intent.putExtra("edit", "warna kulit keliling luka");
                intent.putExtra("id_kajian", id_kajian);
                intent.putExtra("text", textWarnaLuka.getText());
                startActivity(intent);
                finish();
            }
        });

        editGranulasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                String id_kajian = extras.getString("id_kajian");
                Intent intent = new Intent(getApplicationContext(), editSkoringLuka.class);
                intent.putExtra("edit", "kondisi jaringan granulasi");
                intent.putExtra("id_kajian", id_kajian);
                intent.putExtra("text", textGranulasi.getText());
                startActivity(intent);
                finish();
            }
        });

        editEpitelisasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                String id_kajian = extras.getString("id_kajian");
                Intent intent = new Intent(getApplicationContext(), editSkoringLuka.class);
                intent.putExtra("edit", "kondisi jaringan epitel");
                intent.putExtra("id_kajian", id_kajian);
                intent.putExtra("text", textEpitelisasi.getText());
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        InsertLog(id_nurse, "Menekan tombol kembali dari halaman detail kajian");
        Intent i = new Intent(getApplicationContext(), detailKajian.class);
        Bundle extras = getIntent().getExtras();
        String id_kajian = extras.getString("id_kajian");
        i.putExtra("id_kajian", id_kajian);
        startActivity(i);
        finish();
    }


    // cari pasien
    public void getDetail(final String id) {
        Call<KajianResponse> pasienResponseCall = RetrofitClient.getService().cariDetailKajian(id);
        pasienResponseCall.enqueue(new Callback<KajianResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<KajianResponse> call, Response<KajianResponse> response) {

                if (response.isSuccessful()) {
                    //login start main activity




                    //Luas
                    if( response.body().getSize().isEmpty()){
                        textLuas.setText("-");
                    } else{
                        textLuas.setText(response.body().getSize());
                    }


                    //Tepi

                    if( response.body().getEdges().isEmpty()){
                        textTepi.setText("-");
                    } else{
                        textTepi.setText(response.body().getEdges());
                    }


                    //Tipe Nekrotik
                    if( response.body().getNecrotic_type().isEmpty()){
                        textTipeNekrotik.setText("-");
                    } else{
                        textTipeNekrotik.setText(response.body().getNecrotic_type());

                    }


                    //Jumlah Nekrotik
                    if( response.body().getNecrotic_amount().isEmpty()){
                        textJumlahNekrotik.setText("-");
                    } else{
                        textJumlahNekrotik.setText(response.body().getNecrotic_amount());

                    }


                    //Warna Kulit Keliling Luka
                    if( response.body().getSkincolor_surround().isEmpty()){
                        textWarnaLuka.setText("-");
                    } else{
                        textWarnaLuka.setText(response.body().getSkincolor_surround());

                    }


                    //Granulasi
                    if( response.body().getGranulation().isEmpty()){
                        textGranulasi.setText("-");
                    } else{
                        textGranulasi.setText(response.body().getGranulation());
                    }


                    //Epitelisasi
                    if( response.body().getEpithelization().isEmpty()){
                        textEpitelisasi.setText("-");
                    } else{
                        textEpitelisasi.setText(response.body().getEpithelization());

                    }





                } else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<KajianResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}