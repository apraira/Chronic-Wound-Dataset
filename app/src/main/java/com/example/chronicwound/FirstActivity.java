package com.example.chronicwound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_awal);

        final Button button_login = (Button) findViewById(R.id.log_in);
        final Button button_daftar = (Button) findViewById(R.id.buat_akun);

        // button login
        button_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();

            }
        });


        // button daftar
        button_daftar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
                finish();

            }
        });
    }


}