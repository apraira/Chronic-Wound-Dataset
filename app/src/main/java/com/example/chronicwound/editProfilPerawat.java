package com.example.chronicwound;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.pasien.editPasien;
import com.example.chronicwound.pasien.listPasienActivity;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.updatePerawatRequest;
import com.example.chronicwound.tambahpasien.tambahPasienActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chronicwound.logging.LogHelper.InsertLog;

public class editProfilPerawat extends AppCompatActivity {

    //textView
    TextView teksToolBar, teksPerintah;
    TextInputLayout textInputLayoutEdit, textInputLayoutOption, textInputLayoutTanggalLahir;
    TextInputEditText editText, editTextTanggalLahir;
    AutoCompleteTextView editTextOption;
    Button buttonSubmit;
    String jenis, edit_type, teks, siapa, NRM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_perawat);

        teksToolBar = (TextView) findViewById(R.id.teksToolBar);
        teksPerintah = (TextView) findViewById(R.id.teksPerintah);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        editText = (TextInputEditText) findViewById(R.id.editText);
        editTextTanggalLahir = (TextInputEditText) findViewById(R.id.editTextTanggalLahir);
        editTextOption = (AutoCompleteTextView) findViewById(R.id.editTextOption);
        textInputLayoutEdit = (TextInputLayout) findViewById(R.id.textInputLayoutEdit);
        textInputLayoutOption = (TextInputLayout) findViewById(R.id.textInputLayoutOption);
        textInputLayoutTanggalLahir = (TextInputLayout) findViewById(R.id.textInputLayoutTanggalLahir);

        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        NRM = settings.getString("NRM", "");

        //get the current intent
        Intent intent = getIntent();
        edit_type = intent.getStringExtra("edit");
        teks = intent.getStringExtra("text");
        siapa = intent.getStringExtra("siapa");

        editText.setText(teks);

        if (siapa.contains("perawat")){
            teksToolBar.setText("Edit " + edit_type + " perawat");

        }else {
            teksToolBar.setText("Edit " + edit_type + " pasien");
        }


        // special case form
        if (edit_type.contains("agama") || edit_type.contains("jenis kelamin")){
            textInputLayoutEdit.setVisibility(View.GONE);
            textInputLayoutOption.setVisibility(View.VISIBLE);
            editTextOption.setText(teks);

            //array opsi
            //data dropdown
            String[] listAgama = new String[]{"Islam", "Protestan", "Katolik", "Hindu", "Buddha", "Khonghucu"};
            String[] listKelamin = new String[]{"Perempuan", "Laki-laki"};

            if(edit_type.contains("agama")){
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listAgama);
                editTextOption.setAdapter(adapter);
            }else{
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listKelamin);
                editTextOption.setAdapter(adapter);
            }
        }else if (edit_type.contains("tanggal lahir")){
            //tanggal lahir
            textInputLayoutEdit.setVisibility(View.GONE);
            textInputLayoutOption.setVisibility(View.GONE);
            textInputLayoutTanggalLahir.setVisibility(View.VISIBLE);
            editTextTanggalLahir.setText(teks);

            //kalau fieldnya dipencet
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
                            editProfilPerawat.this,
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

        }else{
            textInputLayoutEdit.setVisibility(View.VISIBLE);
            textInputLayoutOption.setVisibility(View.GONE);
        }


        teksPerintah.setText("Silakan input " + edit_type + " baru pada kolom di bawah ini.");

        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if(siapa.contains("perawat")){
                    i = new Intent(getApplicationContext(), ProfilPerawat.class);
                } else{
                    i = new Intent(getApplicationContext(), editPasien.class);
                    SharedPreferences settings = getSharedPreferences("preferences",
                            Context.MODE_PRIVATE);
                    String NRM = settings.getString("NRM", "");
                    i.putExtra("NRM", NRM);
                }
                startActivity(i);
                finish();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("preferences",
                        Context.MODE_PRIVATE);
                String id = settings.getString("_id_perawat", "").toString();
                NRM = settings.getString("id_pasien", "");
                String isian = editText.getText().toString();
                //Check user input is correct or not
                if (validate(isian)) {

                    if (edit_type.contains("nama") && siapa.contains("perawat")){
                        editPerawat(id, "name", isian);
                    } else if(edit_type.toLowerCase().contains("nip")){
                        editPerawat(id, "username", isian);
                        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", isian);
                        editor.commit();
                    } else if (edit_type.contains("e-mail") && siapa.contains("perawat")){
                        editPerawat(id, "email", isian);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Belum diaplikasikan", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        System.out.println("Skip");
                    }
                }
            }
        });
    }

    //This method is used to validate input given by user
    public boolean validate(String username) {

        InsertLog("Sistem", "Validasi isian form edit perawat");
        if(username == null || username.trim().length() == 0){
            textInputLayoutEdit.setError("Maaf, tidak boleh kosong.");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i;
        if(siapa.contains("perawat")){
            i = new Intent(getApplicationContext(), ProfilPerawat.class);
        } else{
            i = new Intent(getApplicationContext(), editPasien.class);
            SharedPreferences settings = getSharedPreferences("preferences",
                    Context.MODE_PRIVATE);
            String NRM = settings.getString("NRM", "");
            i.putExtra("NRM", NRM);
        }
        startActivity(i);
        finish();

    }

    // add pasien
    public void editPerawat(final String id_perawat, final String jenis, final String isian) {
        Call<updatePerawatRequest> pasienResponseCall = RetrofitClient.getService().updatePerawat(id_perawat, jenis, isian);
        pasienResponseCall.enqueue(new Callback<updatePerawatRequest>() {
            @Override
            public void onResponse(Call<updatePerawatRequest> call, Response<updatePerawatRequest> response) {

                if (response.isSuccessful()) {
                    //login start main activity
                    Snackbar.make(buttonSubmit, "Data " + edit_type + " telah di-update.", Snackbar.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), ProfilPerawat.class);
                    startActivity(i);
                    finish();

                } else {
                    Snackbar.make(buttonSubmit, "Data belum berhasil di-update.", Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<updatePerawatRequest> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}