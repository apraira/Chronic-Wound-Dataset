package com.example.chronicwound;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.content.Intent;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chronicwound.remote.LoginResponse;
import com.example.chronicwound.remote.RegisterResponse;
import com.example.chronicwound.remote.ResObj;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UserService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    UserService userService;

    //Declaration EditTexts
    EditText editTextUserName;
    EditText editTextPassword;

    //Declaration TextInputLayout
    TextInputLayout textInputLayoutUserName;
    TextInputLayout textInputLayoutPassword;

    //Declaration Layout
    RelativeLayout loginForm;

    //Declaration Button
    Button buttonLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_login);

        loginForm = findViewById(R.id.loginForm);
        // Check if UserResponse is Already Logged In
        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            loginForm.setVisibility(View.VISIBLE);
        }

        initViews();
        //set click event of login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();

                //Check user input is correct or not
                if (validate(username,password)) {
                    //do login
                    doLogin(username, password);

                }
            }
        });


    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textInputLayoutUserName = (TextInputLayout) findViewById(R.id.textInputLayoutUserName);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        loginForm = findViewById(R.id.loginForm);

    }


    //This method is used to validate input given by user
    public boolean validate(String username, String password) {
        if(username == null || username.trim().length() == 0){
            textInputLayoutUserName.setError("Username tidak boleh kosong");
            return false;
        }
        if(password == null || password.trim().length() == 0){
            textInputLayoutPassword.setError("Password tidak boleh kodong");
            return false;
        }
        return true;
    }

    //do login class
    private void doLogin(final String username,final String password){
        Call<LoginResponse> loginResponseCall = RetrofitClient.getService().login(username,password);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(response.isSuccessful()){
                    //login start main activity
                    Snackbar.make(buttonLogin, "Login success", Snackbar.LENGTH_LONG).show();

                    SaveSharedPreference.setLoggedIn(getApplicationContext(), true);

                    SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", username);
                    editor.commit();

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }else {
                    Snackbar.make(buttonLogin, "Unable to register user, maybe you are already registered", Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}