package com.example.chronicwound;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_login);

        final Button button = (Button) findViewById(R.id.login_masuk);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                Context context = getApplicationContext();
                CharSequence text = "Kamu menekan tombol Log In";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }


}