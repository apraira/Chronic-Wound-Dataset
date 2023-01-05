package com.example.chronicwound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.remote.LoginResponse;
import com.example.chronicwound.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilPerawat extends AppCompatActivity {

    //textView
    TextView textNamaPerawat, textNIPPerawat, textEmailPerawat;

    //Button
    Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_perawat);

        textNamaPerawat = (TextView) findViewById(R.id.textNamaPerawat);
        textNIPPerawat = (TextView) findViewById(R.id.textNIPPerawat);
        textEmailPerawat = (TextView) findViewById(R.id.textEmailPerawat);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        String id_perawat = settings.getString("id_perawat", "").toString();

        cariPerawat(id_perawat);

        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
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


    }



    // cari perawat
    public void cariPerawat(final String username) {
        Call<LoginResponse> loginResponseCall = RetrofitClient.getService().cariIDPerawat(username);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    //login start main activity
                    String nama = response.body().getName();
                    String nip = response.body().getUsername();
                    textNamaPerawat.setText(nama);
                    textNIPPerawat.setText(nip);
                    textEmailPerawat.setText(response.body().getEmail());


                } else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}