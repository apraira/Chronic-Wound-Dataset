package com.example.chronicwound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.anotasi.anotasiTepi;
import com.example.chronicwound.gallery.GaleriActivity;
import com.example.chronicwound.remote.LoginResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.pasien.listPasienActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chronicwound.logging.LogHelper.InsertLog;

public class MainActivity extends AppCompatActivity {
    String UserName;
    private String KEY_USERNAME = "USERNAME";
    private Integer IDperawat;
    private ImageView imageView;
    TextView namaPerawat;
    CircleImageView profil;
    public static String id_nurse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get value of shared preferences
        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        UserName = settings.getString("username", "");


        setNamaPerawat(UserName);

        InsertLog(id_nurse, "Memasuki halaman utama");

        CardView data_pasien = (CardView) findViewById(R.id.data_pasien);
        CardView arsirWarna = (CardView) findViewById(R.id.arsirWarnaLuka);
        CardView galeriLuka = (CardView) findViewById(R.id.galeri_luka);
        profil = (CircleImageView) findViewById(R.id.circle_imageView);

        //Picasso.get().load("https://jft.web.id/woundapi/instance/uploads/43eeaa47-0fb0-4a19-8cda-2a5ee7050eb41663778563.jpg").into(imageView);


        // button login
        data_pasien.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                cariPerawat(UserName);
                InsertLog(id_nurse, "Menekan tombol untuk menuju list pasien");

            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                Intent intent = new Intent(getApplicationContext(), ProfilPerawat.class);
                startActivity(intent);

                InsertLog(id_nurse, "Menekan tombol menuju detail profil perawat");
                finish();

            }
        });
        /*
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set LoggedIn status to false
                SaveSharedPreference.setLoggedIn(getApplicationContext(), false);

                // Logout
                Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        anotasiTepi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.chronicwound.anotasi.anotasiTepi.class);
                startActivity(intent);
            }
        });

        anotasiLuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnotasiActivity.class);
                startActivity(intent);
            }
        });*/


        arsirWarna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.chronicwound.anotasi.anotasiWarna.class);
                intent.putExtra("dari", "utama");
                startActivity(intent);

                InsertLog(id_nurse, "Menekan tombol menuju halaman arsir warna"); 
            }
        });

        galeriLuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertLog(id_nurse, "Menekan tombol untuk memasuki halaman galeri luka");
                Intent intent = new Intent(getApplicationContext(), GaleriActivity.class);
                startActivity(intent);
                finish();
            }
        });








    }

    @Override
    public void onResume(){
        super.onResume();
        // Get value of shared preferences
        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        UserName = settings.getString("username", "");
        setNamaPerawat(UserName);

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    // cari pasien
    public void cariPerawat(final String username) {
        Call<LoginResponse> loginResponseCall = RetrofitClient.getService().cariIDPerawat(username);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                InsertLog(id_nurse, "Melakukan pencarian id perawat untuk disimpan sebagai sharedPreferences");
                if (response.isSuccessful()) {
                    //login start main activity
                    IDperawat = response.body().get_id();
                    System.out.println("Id perawat main activity: " + IDperawat.toString());
                    Intent i = new Intent(getApplicationContext(), listPasienActivity.class);
                    i.putExtra(KEY_USERNAME, IDperawat);
                    SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("id_perawat", IDperawat.toString());
                    editor.commit();



                    startActivity(i);
                    finish();



                } else {
                    Toast.makeText(MainActivity.this, "gagal", Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // cari pasien
    public void setNamaPerawat(final String username) {

        Call<LoginResponse> loginResponseCall = RetrofitClient.getService().cariIDPerawat(username);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    //login start main activity
                    namaPerawat = (TextView) findViewById(R.id.namaPerawat);
                    namaPerawat.setText(response.body().getName());
                    TextView editUsername = (TextView) findViewById(R.id.view_username);
                    editUsername.setText(response.body().getUsername());

                    id_nurse = response.body().get_id().toString();
                    InsertLog(id_nurse, "Mencari id berdasarkan NIP");
                    SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("id_perawat", response.body().get_id().toString());
                    editor.commit();



                } else {
                    Toast.makeText(MainActivity.this, "gagal", Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}