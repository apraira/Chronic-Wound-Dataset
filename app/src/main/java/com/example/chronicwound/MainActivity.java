package com.example.chronicwound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.anotasi.anotasiTepi;
import com.example.chronicwound.remote.LoginResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.pasien.listPasienActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    String UserName;
    private String KEY_USERNAME = "USERNAME";
    private Integer IDperawat;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get value
        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        UserName = settings.getString("username", "");

        final TextView editUsername = (TextView) findViewById(R.id.view_username);
        editUsername.setText(UserName);

        CardView data_pasien = (CardView) findViewById(R.id.data_pasien);

        //Picasso.get().load("https://jft.web.id/woundapi/instance/uploads/43eeaa47-0fb0-4a19-8cda-2a5ee7050eb41663778563.jpg").into(imageView);


        // button login
        data_pasien.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                cariPerawat(UserName);

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









    }

    // cari pasien
    public void cariPerawat(final String username) {
        Call<LoginResponse> loginResponseCall = RetrofitClient.getService().cariIDPerawat(username);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    //login start main activity
                    IDperawat = response.body().get_id();
                    System.out.println("Id perawat main activity: " + IDperawat.toString());
                    Intent i = new Intent(getApplicationContext(), listPasienActivity.class);
                    i.putExtra(KEY_USERNAME, IDperawat);
                    SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("id_perawat", IDperawat);
                    editor.commit();


                    startActivity(i);



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