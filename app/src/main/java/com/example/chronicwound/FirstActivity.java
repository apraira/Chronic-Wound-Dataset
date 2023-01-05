package com.example.chronicwound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.chronicwound.logging.LogHelper.InsertLog;

public class FirstActivity extends AppCompatActivity {

    RelativeLayout FirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_awal);

        FirstTime = findViewById(R.id.FirstTime);

        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            FirstTime.setVisibility(View.VISIBLE);
        }

        InsertLog("Guest", "Memasuki Halaman Pertama");

        final Button button_login = (Button) findViewById(R.id.log_in);
        final Button button_daftar = (Button) findViewById(R.id.buat_akun);

        // button login
        button_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InsertLog("Guest", "Menekan tombol login");
                // your handler code here
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);

            }
        });


        // button daftar
        button_daftar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InsertLog("Guest", "Menekan tombol pendaftaran");
                // your handler code here
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });
    }


}