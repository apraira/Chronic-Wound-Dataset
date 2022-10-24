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

public class tambahKajianEpithelization extends AppCompatActivity {
    AutoCompleteTextView opsiEpithelization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_kajian_epithelization);

        //list opsi-opsi form ephitelization tissue
        opsiEpithelization = (AutoCompleteTextView) findViewById(R.id.editTextEpithelization);
        String[] listEpithelization = new String[]{"1 = 100% wound covered, surface intac", "2 = 75% to <100% wound covered &/or epithelial tissue extends >0.5cm  into wound bed", "3 =  50% to <75% wound covered &/or epithelial tissue extends to <0.5cm  into wound bed", "4 = 25% to < 50% wound covered ", "5 = < 25%  wound covered"};
        ArrayAdapter<String> adapterEpithelization = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listEpithelization);
        opsiEpithelization.setAdapter(adapterEpithelization);

        //get value from previous activity
        Intent intent_camera = getIntent();
        Uri uri = intent_camera.getParcelableExtra("URI");
        String size = intent_camera.getStringExtra("size");
        String edges = intent_camera.getStringExtra("edges");
        String NType = intent_camera.getStringExtra("NType");
        String NAmount = intent_camera.getStringExtra("NAmount");
        String SkinColor = intent_camera.getStringExtra("SkinColor");
        String Granulation = intent_camera.getStringExtra("Granulation");


        Button next = findViewById(R.id.buttonNext);
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), tambahKajianKonfirmasi.class);
                i.putExtra("size", size);
                i.putExtra("URI", uri);
                i.putExtra("edges", edges);
                i.putExtra("NType", NType);
                i.putExtra("NAmount", NAmount);
                i.putExtra("SkinColor", SkinColor);
                i.putExtra("Granulation", Granulation);
                String Epithelization = opsiEpithelization.getText().toString();
                i.putExtra("Epithelization", Epithelization);
                startActivity(i);
            }
        });
    }
}