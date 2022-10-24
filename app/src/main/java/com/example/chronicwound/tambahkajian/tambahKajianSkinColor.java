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

public class tambahKajianSkinColor extends AppCompatActivity {

    AutoCompleteTextView opsiSkinColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_kajian_skin_color);

        //list opsi-opsi form skin color surrounding wound
        opsiSkinColor = (AutoCompleteTextView) findViewById(R.id.editTextSkinColor);
        String[] listSkinColor = new String[]{"1 = Pink or normal for ethnic group", "2 =  Bright red &/or blanches to touch", "3 = White or grey pallor or hypopigmented", "4 =  Dark red or purple &/or non-blanchable", "5 = Black or hyperpigmented"};
        ArrayAdapter<String> adapterSkinColor = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listSkinColor);
        opsiSkinColor.setAdapter(adapterSkinColor);

        //get value from previous activity
        Intent intent_camera = getIntent();
        Uri uri = intent_camera.getParcelableExtra("URI");
        String size = intent_camera.getStringExtra("size");
        String edges = intent_camera.getStringExtra("edges");
        String NType = intent_camera.getStringExtra("NType");
        String NAmount = intent_camera.getStringExtra("NAmount");


        Button next = findViewById(R.id.buttonNext);
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), tambahKajianGranulation.class);
                i.putExtra("size", size);
                i.putExtra("URI", uri);
                i.putExtra("edges", edges);
                i.putExtra("NType", NType);
                i.putExtra("NAmount", NAmount);
                String SkinColor = opsiSkinColor.getText().toString();
                i.putExtra("SkinColor", SkinColor);
                startActivity(i);
            }
        });
    }
}