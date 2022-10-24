package com.example.chronicwound.tambahkajian;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.chronicwound.R;

public class tambahKajianNecroticAmount extends AppCompatActivity {

    AutoCompleteTextView opsiNAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_kajian_necrotic_amount);

        //list opsi-opsi form necrotic amount
        opsiNAmount = (AutoCompleteTextView) findViewById(R.id.editTextNAmount);
        String[] listNAmount = new String[]{"1 = None visible", "2 = <25% of wound bed covered", "3 = 25% to 50% of wound covered", "4 =  > 50% and < 75% of wound covered", "5 =   75% to 100% of wound covered"};
        ArrayAdapter<String> adapterNAmount = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listNAmount);
        opsiNAmount.setAdapter(adapterNAmount);

        //get value from previous activity
        Intent intent_camera = getIntent();
        Uri uri = intent_camera.getParcelableExtra("URI");
        String size = intent_camera.getStringExtra("size");
        String edges = intent_camera.getStringExtra("edges");
        String NType = intent_camera.getStringExtra("NType");


        Button next = findViewById(R.id.buttonNext);
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), tambahKajianSkinColor.class);
                i.putExtra("size", size);
                i.putExtra("URI", uri);
                i.putExtra("edges", edges);
                i.putExtra("NType", NType);
                String NAmount = opsiNAmount.getText().toString();
                i.putExtra("NAmount", NAmount);
                startActivity(i);
            }
        });
    }
}