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

public class tambahKajianNecroticType extends AppCompatActivity {

    AutoCompleteTextView opsiNType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_kajian_necrotic_type);

        //list opsi-opsi form necrotic type
        opsiNType = (AutoCompleteTextView) findViewById(R.id.editTextNType);
        String[] listNType = new String[]{"1 =  None visible", "2 = White/grey non-viable tissue &/or non-adherent yellow slough", "3 =  Loosely adherent yellow slough", "4 = Adherent, soft, black eschar", "5 =  Firmly adherent, hard, black eschar"};
        ArrayAdapter<String> adapterNType = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listNType);
        opsiNType.setAdapter(adapterNType);

        //get value from previous activity
        Intent intent_camera = getIntent();
        Uri uri = intent_camera.getParcelableExtra("URI");
        String size = intent_camera.getStringExtra("size");
        String edges = intent_camera.getStringExtra("edges");

        Button next = findViewById(R.id.buttonNext);
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), tambahKajianNecroticAmount.class);
                i.putExtra("size", size);
                i.putExtra("URI", uri);
                i.putExtra("edges", edges);
                String NType = opsiNType.getText().toString();
                i.putExtra("NType", NType);
                startActivity(i);
            }
        });
    }
}