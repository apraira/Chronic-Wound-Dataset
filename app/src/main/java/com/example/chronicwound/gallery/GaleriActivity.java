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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

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
    private Chip chipRaw, chipTepi, chipDiameter, chipArsir;
    private ChipGroup chipFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        InsertLog(id_nurse, "Memasuki halaman galeri luka");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeri);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        chipRaw = (Chip) findViewById(R.id.chipRaw);
        chipTepi = (Chip) findViewById(R.id.chipTepi);
        chipDiameter = (Chip) findViewById(R.id.chipDiameter);
        chipArsir = (Chip) findViewById(R.id.chipArsir);
        chipFilter = (ChipGroup) findViewById(R.id.filterPasien);

        chipTepi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter("Anotasi Tepi");
            }
        });

        chipArsir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter("Arsir Warna");
            }
        });

        chipDiameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter("Anotasi Diameter");
            }
        });

        chipRaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter("Raw");
            }
        });


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

    @Override
    public void onResume() {
        super.onResume();
        chipFilter.clearCheck();
        getAllImages();



    }

    private void filter(String text){
        // creating a new array list to filter our data.
        ArrayList<GalleryRequest> filteredlist = new ArrayList<GalleryRequest>();

        // running a for loop to compare elements.
        for (GalleryRequest item : imageArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getCategory() == null){System.out.println("Null");}else{
                if (item.getCategory().toLowerCase().contains(text.toLowerCase())) {
                    // if the item is matched we are
                    // adding it to our filtered list.
                    filteredlist.add(item);
                }
            }

        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(getApplicationContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
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