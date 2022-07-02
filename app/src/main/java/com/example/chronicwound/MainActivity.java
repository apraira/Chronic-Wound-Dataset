package com.example.chronicwound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String UserName;


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

        Button logout = (Button) findViewById(R.id.logout);
        Button anotasi = (Button) findViewById(R.id.anotasi);


        // button login
        anotasi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),AnotasiActivity.class);
                startActivity(i);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
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


        /**
         * Logout
         * TODO: Please modify according to your need it is just an example
         */

    }
}