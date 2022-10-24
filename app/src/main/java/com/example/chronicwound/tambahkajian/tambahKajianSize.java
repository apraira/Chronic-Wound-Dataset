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

public class tambahKajianSize extends AppCompatActivity {

    AutoCompleteTextView opsiSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_kajian_size);

        //list opsi-opsi form size
        opsiSize = (AutoCompleteTextView) findViewById(R.id.editTextSize);
        String[] listSize = new String[]{"1= pxl < 4sq cm", "2= pxl  4=<16sq cm", "3= pxl 16.1=<36 sq cm ", "4= pxl 36.1--<80 sq cm", "5 = pxl >80 sq cm"};
        ArrayAdapter<String> adapterSize = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listSize);
        opsiSize.setAdapter(adapterSize);

        //get value from previous activity
        Intent intent_camera = getIntent();
        Uri uri = intent_camera.getParcelableExtra("URI");


        Button next = findViewById(R.id.buttonNext);
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), tambahKajianEdges.class);
                String size = opsiSize.getText().toString();
                i.putExtra("size", size);
                i.putExtra("URI", uri);
                startActivity(i);
            }
        });

    }
}