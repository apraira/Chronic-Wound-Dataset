package com.example.chronicwound.tambahkajian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chronicwound.R;

public class tambahKajianKonfirmasi extends AppCompatActivity {

    TextView textSize, textEdges, textNType, textNAmount, textSkinColor, textGranulation, textEpithelization;
    ImageView fotoRaw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_kajian_konfirmasi);

        textSize = (TextView) findViewById(R.id.textSize);
        textEdges = (TextView) findViewById(R.id.textEdges);
        textNType = (TextView) findViewById(R.id.textNType);
        textNAmount = (TextView) findViewById(R.id.textNAmount);
        textSkinColor = (TextView) findViewById(R.id.textSkinColor);
        textGranulation = (TextView) findViewById(R.id.textGranulation);
        textEpithelization = (TextView) findViewById(R.id.textEpithelization);
        fotoRaw = (ImageView)findViewById(R.id.rawImageView);

        //get value from previous activity
        Intent intent_camera = getIntent();
        Uri uri = intent_camera.getParcelableExtra("URI");
        String size = intent_camera.getStringExtra("size");
        String edges = intent_camera.getStringExtra("edges");
        String NType = intent_camera.getStringExtra("NType");
        String NAmount = intent_camera.getStringExtra("NAmount");
        String SkinColor = intent_camera.getStringExtra("SkinColor");
        String Granulation = intent_camera.getStringExtra("Granulation");
        String Epithelization = intent_camera.getStringExtra("Epithelization");

        //set text
        fotoRaw.setImageURI(uri);
        textSize.setText(size);
        textEdges.setText(edges);
        textNType.setText(NType);
        textNAmount.setText(NAmount);
        textSkinColor.setText(SkinColor);
        textGranulation.setText(Granulation);
        textEpithelization.setText(Epithelization);


    }
}