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

public class tambahKajianEdges extends AppCompatActivity {
    AutoCompleteTextView opsiEdges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_kajian_edges);

        //list opsi-opsi form edges
        opsiEdges = (AutoCompleteTextView) findViewById(R.id.editTextEdges);
        String[] listEdges = new String[]{"1 = Indistinct, diffuse, none clearly visible", "2 = Distinct, outline clearly visible, attached, even with wound base", "3 = Well-defined, not attached to wound base", "4 = Well-defined, not attached to base, rolled under, thickened", "5 = Well-defined, fibrotic, scarred or hyperkeratotic"};
        ArrayAdapter<String> adapterEdges = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listEdges);
        opsiEdges.setAdapter(adapterEdges);

        //get value from previous activity
        Intent intent_camera = getIntent();
        Uri uri = intent_camera.getParcelableExtra("URI");
        String size = intent_camera.getStringExtra("size");


        Button next = findViewById(R.id.buttonNext);
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), tambahKajianNecroticType.class);
                i.putExtra("size", size);
                i.putExtra("URI", uri);
                String edges = opsiEdges.getText().toString();
                i.putExtra("edges", edges);
                startActivity(i);
            }
        });

    }
}