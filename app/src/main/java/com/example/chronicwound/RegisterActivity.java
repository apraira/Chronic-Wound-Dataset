package com.example.chronicwound;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chronicwound.remote.RegisterResponse;
import com.example.chronicwound.remote.ResObj;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UserService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chronicwound.logging.LogHelper.InsertLog;

public class RegisterActivity extends AppCompatActivity {

    //Declaration EditTexts
    EditText editTextUserName;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextNama;

    //Declaration TextInputLayout
    TextInputLayout textInputLayoutUserName;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;
    TextInputLayout textInputLayoutNama;

    //Declaration Button
    Button buttonRegister;

    UserService userService;


    //Declaration Layout
    RelativeLayout registForm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_register);
        InsertLog("Guest", "Memasuki Halaman Buat Akun");

        registForm = findViewById(R.id.registForm);
        // Check if UserResponse is Already Logged In
        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            registForm.setVisibility(View.VISIBLE);
        }

        initViews();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                InsertLog("Guest", "Menekan tombol pembuatan akun pada halaman Buat Akun");
                if (validate()) {
                    String UserName = editTextUserName.getText().toString();
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();
                    String Nama = editTextNama.getText().toString();

                    doRegister(Nama, UserName, Email, Password);


                }
            }
        });
    }



    //this method is used to connect XML views to its Objects
    private void initViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextNama = (EditText) findViewById(R.id.editNama);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutUserName = (TextInputLayout) findViewById(R.id.textInputLayoutUserName);
        textInputLayoutNama = (TextInputLayout) findViewById(R.id.textInputLayoutNama);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);


    }

    //This method is used to validate input given by user
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public boolean validate() {
        boolean valid = false;
        InsertLog("Sistem", "Memvalidasi isian form");

        //Get values from EditText fields
        String Nama = editTextNama.getText().toString();
        String UserName = editTextUserName.getText().toString();
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();

        //Handling validation for UserName field

        if (UserName.isEmpty()) {
            valid = false;
            textInputLayoutUserName.setError("Username tidak boleh kosong");
        } else {
            if (UserName.matches("^(?=.{0,10}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")) {
                valid = true;
                textInputLayoutUserName.setError(null);
            } else {
                valid = false;
                textInputLayoutUserName.setError("Tidak boleh selain abjad dan angka");
            }
        }

        //Handling validation for Email field
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            textInputLayoutEmail.setError("Email tidak boleh kosong");
        } else {
            valid = true;
            textInputLayoutEmail.setError(null);
        }


        //Nama
        if(Nama.isEmpty()){
            textInputLayoutNama.setError("Nama tidak boleh kosong");
            valid = false;
        }else {
            valid = true;
            textInputLayoutEmail.setError(null);
        }



        //Handling validation for Password field
        if (Password.isEmpty()) {
            valid = false;
            textInputLayoutPassword.setErrorEnabled(true);
            textInputLayoutPassword.setError("Password tidak boleh kosong");


        } else {
            if (Password.length() > 5) {
                valid = true;
                textInputLayoutPassword.setError(null);
            } else {
                valid = false;
                textInputLayoutPassword.setErrorEnabled(true);
                textInputLayoutPassword.setError("Minimal 6 karakter");
            }
        }


        return valid;
    }

    // do register
    public void doRegister(final String name, final String username, final String email,final String password){
        InsertLog("Guest", "Data pendaftaran akan diunggah ke database");
        Call<RegisterResponse> registerResponseCall = RetrofitClient.getService().signup(name,username,email,password);
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                if(response.isSuccessful()){
                    //login start main activity
                    Snackbar.make(buttonRegister, "User created successfully!", Snackbar.LENGTH_LONG).show();

                    SaveSharedPreference.setLoggedIn(getApplicationContext(), true);

                    SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", username);
                    editor.commit();
                    InsertLog("Guest", "Guest berhasil mendaftarkan akun baru");
                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }else {
                    Snackbar.make(buttonRegister, "Unable to register user, maybe you are already registered", Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}