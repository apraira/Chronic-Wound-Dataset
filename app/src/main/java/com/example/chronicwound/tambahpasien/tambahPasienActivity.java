package com.example.chronicwound.tambahpasien;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.chronicwound.R;
import com.example.chronicwound.model;
import com.example.chronicwound.pasien.listPasienActivity;
import com.example.chronicwound.remote.PasienRequest;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UserService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

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
    EditText editTextTanggalLahir;
    EditText editTextAlamat,editTextNoHp, editTextEmail;

    //Declaration TextInputLayout
    TextInputLayout textInputLayoutNama;
    TextInputLayout textInputLayoutNRM;
    TextInputLayout textInputLayoutUsia;
    TextInputLayout textInputLayoutTanggalLahir, textInputLayoutKelamin, textInputLayoutAlamat, textInputLayoutNoHp, textInputLayoutEmail;

    //Dropdown
    AutoCompleteTextView OpsiAgama, OpsiKelamin;

    //Declaration Button
    Button buttonSubmit;



    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_tambahpasien);
        initViews();


        //retrieve ID Perawat
        Bundle extras = getIntent().getExtras();
        IDperawat = extras.getInt(KEY_USERNAME);
        System.out.println("Id perawat tambah pasien: " + IDperawat.toString());


        //data dropdown
        String[] listAgama = new String[]{"Islam", "Protestan", "Katolik", "Hindu", "Buddha", "Khonghucu"};
        String[] listKelamin = new String[]{"Perempuan", "Laki-laki"};

        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listAgama);
        OpsiAgama.setAdapter(adapter);

        //to get selected value dari dropdown
        OpsiAgama.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "" + OpsiAgama.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //Adapter untuk list kelamin
        ArrayAdapter<String> adapterKelamin = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listKelamin);
        OpsiKelamin.setAdapter(adapterKelamin);

        //to get selected value dari dropdown
        OpsiKelamin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "" + OpsiKelamin.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        editTextTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        tambahPasienActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                editTextTanggalLahir.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }


        });







        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {




                    String nrm = editTextNRM.getText().toString();
                    String id_perawat = IDperawat.toString();
                    String nama = editTextNama.getText().toString();
                    String agama = OpsiAgama.getText().toString();
                    String born_date = editTextTanggalLahir.getText().toString();
                    String usia = editTextUsia.getText().toString();
                    String kelamin  = OpsiKelamin.getText().toString();
                    String alamat = editTextAlamat.getText().toString();
                    String no_hp = editTextNoHp.getText().toString();
                    String email = editTextEmail.getText().toString();



                    //PasienRequest pasien = new PasienRequest(nrm, id_perawat, nama, agama, born_date, usia, kelamin, alamat, no_hp, email);
                    Call<PasienResponse> call = RetrofitClient.getService().createPasien(nrm, id_perawat, nama, agama, born_date, usia, kelamin, alamat, no_hp, email);
                    call.enqueue(new Callback<PasienResponse>() {
                        @Override
                        public void onResponse(Call<PasienResponse> call, Response<PasienResponse> response) {
                            if(response.isSuccessful()){
                                //login start main activity
                                Snackbar.make(buttonSubmit, "Patient created successfully!", Snackbar.LENGTH_LONG).show();
                                Intent i = new Intent(tambahPasienActivity.this, listPasienActivity.class);
                                i.putExtra(KEY_USERNAME, IDperawat);
                                startActivity(i);
                                finish();

                            }else {
                                Snackbar.make(buttonSubmit, "Unable to add new patient", Snackbar.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<PasienResponse> call, Throwable t) {
                            call.cancel();
                            Toast.makeText(getApplicationContext(), "input pasien gagal", Toast.LENGTH_SHORT).show();
                        }
                    });

            }
        });
    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        OpsiAgama = (AutoCompleteTextView) findViewById(R.id.editTextAgama);
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextNRM = (EditText) findViewById(R.id.editTextNRM);
        editTextUsia = (EditText) findViewById(R.id.editTextUsia);
        editTextTanggalLahir = (EditText) findViewById(R.id.editTextTanggalLahir);
        OpsiKelamin = (AutoCompleteTextView) findViewById(R.id.editTextKelamin);
        editTextAlamat = (EditText) findViewById(R.id.editTextAlamat);
        editTextNoHp = (EditText) findViewById(R.id.editTextNoHp);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        textInputLayoutNama = (TextInputLayout) findViewById(R.id.textInputLayoutNama);
        textInputLayoutNRM = (TextInputLayout) findViewById(R.id.textInputLayoutNRM);
        textInputLayoutUsia = (TextInputLayout) findViewById(R.id.textInputLayoutUsia);
        textInputLayoutTanggalLahir = (TextInputLayout) findViewById(R.id.textInputLayoutTanggalLahir);
        textInputLayoutKelamin = (TextInputLayout) findViewById(R.id.textInputLayoutKelamin);
        textInputLayoutAlamat = (TextInputLayout) findViewById(R.id.textInputLayoutAlamat);
        textInputLayoutNoHp = (TextInputLayout) findViewById(R.id.textInputLayoutNoHp);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);

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

