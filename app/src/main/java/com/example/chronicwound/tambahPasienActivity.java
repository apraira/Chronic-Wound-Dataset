package com.example.chronicwound;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UserService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class tambahPasienActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PasienAdapter adapter;
    private ArrayList<model> mahasiswaArrayList;
    private Integer IDperawat;
    private String KEY_USERNAME = "USERNAME";

    //Declaration EditTexts
    EditText editTextNama;
    EditText editTextNRM;
    EditText editTextUsia;
    EditText editTextBB;
    EditText editTextTB;

    //Declaration TextInputLayout
    TextInputLayout textInputLayoutNama;
    TextInputLayout textInputLayoutNRM;
    TextInputLayout textInputLayoutUsia;
    TextInputLayout textInputLayoutBB, textInputLayoutTB;

    //Declaration Button
    Button buttonSubmit;

    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_tambahpasien);

        initViews();
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {

                    Bundle extras = getIntent().getExtras();
                    IDperawat = extras.getInt(KEY_USERNAME);

                    String Nama = editTextNama.getText().toString();
                    Integer id_perawat = IDperawat;
                    String nrm = editTextNRM.getText().toString();
                    String usia = editTextUsia.getText().toString();
                    String bb = editTextBB.getText().toString();
                    String tb = editTextTB.getText().toString();

                    addPasien(Nama, id_perawat, nrm, usia, bb, tb );



            }
        });
    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextNRM = (EditText) findViewById(R.id.editTextNRM);
        editTextUsia = (EditText) findViewById(R.id.editTextUsia);
        editTextBB = (EditText) findViewById(R.id.editTextBB);
        editTextTB = (EditText) findViewById(R.id.editTextTB);

        textInputLayoutNama = (TextInputLayout) findViewById(R.id.textInputLayoutNama);
        textInputLayoutNRM = (TextInputLayout) findViewById(R.id.textInputLayoutNRM);
        textInputLayoutUsia = (TextInputLayout) findViewById(R.id.textInputLayoutUsia);
        textInputLayoutBB = (TextInputLayout) findViewById(R.id.textInputLayoutBB);
        textInputLayoutTB = (TextInputLayout) findViewById(R.id.textInputLayoutTB);

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
    }

    // add pasien
    public void addPasien(final String nama, final Integer id, final String nrm, final String usia,final String bb, final String tb){
        Call<PasienResponse> pasienResponseCall = RetrofitClient.getService().addPasien(nrm,id,nama,usia,bb,tb);
        pasienResponseCall.enqueue(new Callback<PasienResponse>() {
            @Override
            public void onResponse(Call<PasienResponse> call, Response<PasienResponse> response) {

                if(response.isSuccessful()){
                    //login start main activity
                    Snackbar.make(buttonSubmit, "Patient created successfully!", Snackbar.LENGTH_LONG).show();
                    Intent i = new Intent(tambahPasienActivity.this, listPasienActivity.class);
                    startActivity(i);
                    finish();

                }else {
                    Snackbar.make(buttonSubmit, "Unable to add new patient", Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<PasienResponse> call, Throwable t) {
                Toast.makeText(tambahPasienActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


}}

