package com.example.chronicwound.gallery;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.example.chronicwound.R;
import com.example.chronicwound.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chronicwound.MainActivity.id_nurse;
import static com.example.chronicwound.logging.LogHelper.InsertLog;


public class GaleriActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private com.example.chronicwound.gallery.AllGalleryAdapter adapter;
    private ArrayList<GalleryRequest> imageArrayList;
    private Integer IDperawat;
    private String KEY_NAME = "NRM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        InsertLog(id_nurse, "Memasuki halaman galeri luka");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeri);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        getAllImages();

        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //



    }

    /**
    void addData(){
        imageArrayList = new ArrayList<>();
        imageArrayList.add(new imageModel ("https://jft.web.id/woundapi/instance/uploads/048fefe2481e8aea2cd3441ae1028ad61664783158.jpg"));
        imageArrayList.add(new imageModel ("https://jft.web.id/woundapi/instance/uploads/43eeaa47-0fb0-4a19-8cda-2a5ee7050eb41663778563.jpg"));
    }**/

    private void getAllImages() {
        Call<ArrayList<GalleryRequest>> pasienResponseCall = RetrofitClient.getService().getAllImages();

        // on below line we are calling method to enqueue and calling
        // all the data from array list.
        pasienResponseCall.enqueue(new Callback<ArrayList<GalleryRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<GalleryRequest>> call, Response<ArrayList<GalleryRequest>> response) {
                // inside on response method we are checking
                // if the response is success or not.
                if (response.isSuccessful()) {

                    // below line is to add our data from api to our array list.
                    imageArrayList = response.body();

                    // below line we are running a loop to add data to our adapter class.
                    for (int i = 0; i < imageArrayList.size(); i++) {
                        adapter = new AllGalleryAdapter(imageArrayList, GaleriActivity.this);

                        int numberOfColumns = 2;

                        adapter = new com.example.chronicwound.gallery.AllGalleryAdapter(imageArrayList, com.example.chronicwound.gallery.GaleriActivity.this);

                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(com.example.chronicwound.gallery.GaleriActivity.this, numberOfColumns);

                        recyclerView.setLayoutManager(layoutManager);

                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GalleryRequest>> call, Throwable t) {
                // in the method of on failure we are displaying a
                // toast message for fail to get data.
                Toast.makeText(GaleriActivity.this, "Fail to get data", Toast.LENGTH_SHORT).show();
            }
        });


    }


    }