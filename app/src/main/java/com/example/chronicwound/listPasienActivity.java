package com.example.chronicwound;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.example.chronicwound.remote.PasienRequest;
import com.example.chronicwound.remote.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class listPasienActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PasienAdapter adapter;
    private ArrayList<PasienRequest> pasienArrayList;
    private Integer IDperawat;
    private String KEY_USERNAME = "USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_listpasien);

        FloatingActionButton tambah = (FloatingActionButton) findViewById(R.id.tambah_pasien);
        Bundle extras = getIntent().getExtras();
        IDperawat = extras.getInt(KEY_USERNAME);
        //mahasiswaArrayList.clear();

        getAllCourses();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new PasienAdapter(pasienArrayList, listPasienActivity.this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(listPasienActivity.this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        tambah.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), tambahPasienActivity.class);
                i.putExtra(KEY_USERNAME, IDperawat);
                startActivity(i);
            }
        });
    }


    private void getAllCourses() {
        Call<ArrayList<PasienRequest>> pasienResponseCall = RetrofitClient.getService().getAllCourses();

        // on below line we are calling method to enqueue and calling
        // all the data from array list.
        pasienResponseCall.enqueue(new Callback<ArrayList<PasienRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<PasienRequest>> call, Response<ArrayList<PasienRequest>> response) {
                // inside on response method we are checking
                // if the response is success or not.
                if (response.isSuccessful()) {

                    // below line is to add our data from api to our array list.
                    pasienArrayList = response.body();

                    // below line we are running a loop to add data to our adapter class.
                    for (int i = 0; i < pasienArrayList.size(); i++) {
                        adapter = new PasienAdapter(pasienArrayList, listPasienActivity.this);

                        // below line is to set layout manager for our recycler view.
                        LinearLayoutManager manager = new LinearLayoutManager(listPasienActivity.this);

                        // setting layout manager for our recycler view.
                        recyclerView.setLayoutManager(manager);

                        // below line is to set adapter to our recycler view.
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PasienRequest>> call, Throwable t) {
                // in the method of on failure we are displaying a
                // toast message for fail to get data.
                Toast.makeText(listPasienActivity.this, "Fail to get data", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
