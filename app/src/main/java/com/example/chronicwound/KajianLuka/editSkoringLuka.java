package com.example.chronicwound.KajianLuka;

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

import com.example.chronicwound.R;
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

public class editSkoringLuka extends AppCompatActivity {

    //textView
    TextView teksToolBar, teksPerintah;
    TextInputLayout textInputLayoutEdit, textInputLayoutOption, textInputLayoutTanggalLahir;
    TextInputEditText editText, editTextTanggalLahir;
    AutoCompleteTextView editTextOption;
    Button a;
    String jenis, edit_type, teks, siapa, NRM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_perawat);

        teksToolBar = (TextView) findViewById(R.id.teksToolBar);
        teksPerintah = (TextView) findViewById(R.id.teksPerintah);
        a = (Button) findViewById(R.id.buttonSubmit);
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

        teksToolBar.setText("Edit skoring " + edit_type);

        textInputLayoutEdit.setVisibility(View.GONE);
        textInputLayoutOption.setVisibility(View.VISIBLE);
        editTextOption.setText(teks);

        //array opsi
        //data dropdown
        String[] listSize = new String[]{ "1= kurang dari 4cm²", "2= antara 4cm² dan 16cm²", "3= antara 16cm² dan 36cm²", "4= antara 36cm² dan 80cm²", "5 = lebih dari 80cm²"};
        String[] listKelamin = new String[]{"Belum ada", "Belum ada 2"};
        String[] listEdges = new String[]{"1 = Samar, tidak terlihat dengan jelas", "2 = Batas tepi terlihat \nmenyatu dengan dasar luka", "3 = Jelas, tidak menyatu\ndasar luka", "4 = Jelas, tidak menyatu dengan dasar luka, tebal", "5 = Jelas, fibrotik, parut tebal/hiperkeratonik"};
        String[] listNType = new String[]{"1 =  Tidak ada", "2 = Putih/abu-abu, tidak dapat teramati,\ndan atau jaringan kekuningan yang mudah dilepas", "3 =  Jaringan nekrotik kekuningan\nyang melekat tapi mudah dilepas", "4 = Melekat, lembut, eskar hitam", "5 =  Melekat kuat, keras, eskar hitam"};
        String[] listNAmount = new String[]{"1 = Tidak ada", "2 = <25% permukaan luka tertutup\njaringan nekrotik", "3 = 25% sampai 50%\npermukaan luka tertutup", "4 =  50% sampai 75%\npermukaan luka tertutup", "5 =   75% sampai 100%\npermukaan luka tertutup"};
        String[] listSkinColor = new String[]{"1 = Pink atau normal", "2 =  Merah terang jika disentuh", "3 = Putih atau abu-abu,\npucat atau hipopigmentasi", "4 =  Merah gelap atau ungu,\ntidak pucat jika disentuh", "5 = Hitam atau hiperpigmentasi"};
        String[] listGranulation = new String[]{"1 =  Kulit utuh atau\nluka pada sebagian kulit", "2 =  Terang, merah seperti daging;\n75% s/d 100% luka terisi granulasi,\natau jaringan tumbuh.", "3 =  Terang, merah seperti daging;\n<75% dan > 25% luka terisi granulasi", "4 = Pink, dan atau pucat, merah kehitaman\ndan atau luka < 25% terisi granulasi.", "5 = Tidak ada"};
        String[] listEpithelization = new String[]{"1 = 100% luka tertutup,\npermukaan utuh", "2 = 75% s/d 100% epitelisasi", "3 =  50% s/d 75% epitelisasi", "4 = 25% s/d 50% epitelisasi", "5 = < 25%  epitelisasi"};


        // special case form
        if (edit_type.contains("luas luka")) {
            ArrayAdapter<String> adapter = new
                    ArrayAdapter<>(this, R.layout.list_opsi_agama, listSize);
            editTextOption.setAdapter(adapter);
        } else if (edit_type.contains("tepi luka")) {
            ArrayAdapter<String> adapter = new
                    ArrayAdapter<>(this, R.layout.list_opsi_agama, listEdges);
            editTextOption.setAdapter(adapter);
        } else if (edit_type.contains("tipe nekrotik")) {
            ArrayAdapter<String> adapter = new
                    ArrayAdapter<>(this, R.layout.list_opsi_agama, listNType);
            editTextOption.setAdapter(adapter);
        } else if (edit_type.contains("jumlah nekrotik")) {
            ArrayAdapter<String> adapter = new
                    ArrayAdapter<>(this, R.layout.list_opsi_agama, listNAmount);
            editTextOption.setAdapter(adapter);
        } else if (edit_type.contains("warna")) {
            ArrayAdapter<String> adapter = new
                    ArrayAdapter<>(this, R.layout.list_opsi_agama, listSkinColor);
            editTextOption.setAdapter(adapter);
        } else if (edit_type.contains("granulasi")) {
            ArrayAdapter<String> adapter = new
                    ArrayAdapter<>(this, R.layout.list_opsi_agama, listGranulation);
            editTextOption.setAdapter(adapter);
        } else if (edit_type.contains("epitel")) {
            ArrayAdapter<String> adapter = new
                    ArrayAdapter<>(this, R.layout.list_opsi_agama, listEpithelization);
            editTextOption.setAdapter(adapter);
        } else {
            ArrayAdapter<String> adapter = new
                    ArrayAdapter<>(this, R.layout.list_opsi_agama, listKelamin);
            editTextOption.setAdapter(adapter);
        }


        teksPerintah.setText("Silakan input " + edit_type + " baru pada kolom di bawah ini.");

        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(getApplicationContext(), editKajianLuka.class);
                Bundle extras = getIntent().getExtras();
                String id_kajian = extras.getString("id_kajian");
                i.putExtra("id_kajian", id_kajian);
                startActivity(i);
                finish();
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("preferences",
                        Context.MODE_PRIVATE);
                String id = settings.getString("_id_perawat", "").toString();
                NRM = settings.getString("id_pasien", "");
                String isian = editTextOption.getText().toString();
                Bundle extras = getIntent().getExtras();
                String id_kajian = extras.getString("id_kajian");
                //Check user input is correct or not
                if (validate(isian)) {
                    if (edit_type.contains("luas luka")) {
                        editKajian(id_kajian, "size", isian);
                    } else if (edit_type.contains("tepi luka")) {
                        editKajian(id_kajian, "edges", isian);
                    } else if (edit_type.contains("tipe nekrotik")) {
                        editKajian(id_kajian, "necrotic_type", isian);
                    } else if (edit_type.contains("jumlah nekrotik")) {
                        editKajian(id_kajian, "necrotic_amount", isian);
                    } else if (edit_type.contains("warna")) {
                        editKajian(id_kajian, "skincolor_surround", isian);
                    } else if (edit_type.contains("granulasi")) {
                        editKajian(id_kajian, "granulation", isian);
                    } else if (edit_type.contains("epitel")) {
                        editKajian(id_kajian, "epithelization", isian);
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
        i = new Intent(getApplicationContext(), editKajianLuka.class);
        Bundle extras = getIntent().getExtras();
        String id_kajian = extras.getString("id_kajian");
        i.putExtra("id_kajian", id_kajian);
        startActivity(i);
        finish();

    }

    // edit Perawat
    public void editKajian(final String id_perawat, final String jenis, final String isian) {
        Call<updatePerawatRequest> pasienResponseCall = RetrofitClient.getService().updateKajian(id_perawat, jenis, isian);
        pasienResponseCall.enqueue(new Callback<updatePerawatRequest>() {
            @Override
            public void onResponse(Call<updatePerawatRequest> call, Response<updatePerawatRequest> response) {

                if (response.isSuccessful()) {
                    //login start main activity
                    Snackbar.make(a, "Data " + edit_type + " telah di-update.", Snackbar.LENGTH_LONG).show();
                    Intent i;
                    i = new Intent(getApplicationContext(), editKajianLuka.class);
                    Bundle extras = getIntent().getExtras();
                    String id_kajian = extras.getString("id_kajian");
                    i.putExtra("id_kajian", id_kajian);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Data belum berhasil diupdate", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<updatePerawatRequest> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}