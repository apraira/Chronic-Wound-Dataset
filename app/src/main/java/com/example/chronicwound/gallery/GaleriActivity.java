package com.example.chronicwound.gallery;
import android.os.Bundle;
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


public class GaleriActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private com.example.chronicwound.gallery.ImageAdapter adapter;
    private ArrayList<GalleryRequest> imageArrayList;
    private Integer IDperawat;
    private String KEY_NAME = "NRM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeri);

        recyclerView = (RecyclerView) findViewById(R.id.rv_images);

        Bundle extras = getIntent().getExtras();
        String NRM = extras.getString(KEY_NAME);

        //getImageURL(NRM);



    }

    /**
    void addData(){
        imageArrayList = new ArrayList<>();
        imageArrayList.add(new imageModel ("https://jft.web.id/woundapi/instance/uploads/048fefe2481e8aea2cd3441ae1028ad61664783158.jpg"));
        imageArrayList.add(new imageModel ("https://jft.web.id/woundapi/instance/uploads/43eeaa47-0fb0-4a19-8cda-2a5ee7050eb41663778563.jpg"));
    }**/
    /*
    private void getImageURL(String id_pasien) {
        Call<ArrayList<GalleryRequest>> pasienResponseCall = RetrofitClient.getService().getImageByID(id_pasien);

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
                        adapter = new ImageAdapter(imageArrayList, GaleriActivity.this);

                        int numberOfColumns = 2;

                        adapter = new com.example.chronicwound.gallery.ImageAdapter(imageArrayList, com.example.chronicwound.gallery.GaleriActivity.this);

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

    */
    }