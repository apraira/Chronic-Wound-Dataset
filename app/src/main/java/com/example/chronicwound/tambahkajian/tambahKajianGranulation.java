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

public class tambahKajianGranulation extends AppCompatActivity {
    AutoCompleteTextView opsiGranulation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_kajian_granulation);

        //list opsi-opsi form granulation tissue
        opsiGranulation = (AutoCompleteTextView) findViewById(R.id.editTextGranulation);
        String[] listGranulation = new String[]{"1 =  Skin intact or partial thickness wound", "2 =  Bright, beefy red; 75% to 100% of wound filled &/or tissue overgrowth", "3 =  Bright, beefy red; < 75% & > 25% of wound filled", "4 =   Pink, &/or dull, dusky red &/or fills < 25% of wound", "5 = No granulation tissue present"};
        ArrayAdapter<String> adapterGranulation = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listGranulation);
        opsiGranulation.setAdapter(adapterGranulation);

        //get value from previous activity
        Intent intent_camera = getIntent();
        Uri uri = intent_camera.getParcelableExtra("URI");
        String size = intent_camera.getStringExtra("size");
        String edges = intent_camera.getStringExtra("edges");
        String NType = intent_camera.getStringExtra("NType");
        String NAmount = intent_camera.getStringExtra("NAmount");
        String SkinColor = intent_camera.getStringExtra("SkinColor");

        Button next = findViewById(R.id.buttonNext);
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), tambahKajianEpithelization.class);
                i.putExtra("size", size);
                i.putExtra("URI", uri);
                i.putExtra("edges", edges);
                i.putExtra("NType", NType);
                i.putExtra("NAmount", NAmount);
                i.putExtra("SkinColor", SkinColor);
                String Granulation = opsiGranulation.getText().toString();
                i.putExtra("Granulation", Granulation);
                startActivity(i);
            }
        });
    }
}