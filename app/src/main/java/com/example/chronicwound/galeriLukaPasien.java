package com.example.chronicwound;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.gallery.GaleriActivity;
import com.example.chronicwound.gallery.GalleryRequest;
import com.example.chronicwound.gallery.ImageAdapter;
import com.example.chronicwound.pasien.detailPasienActivity;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link galeriLukaPasien#newInstance} factory method to
 * create an instance of this fragment.
 */
public class galeriLukaPasien extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Chip chipRaw, chipTepi, chipDiameter, chipAll;
    private String mParam2;
    private String NRM, id_perawat;
    private RecyclerView recyclerView;
    private com.example.chronicwound.gallery.ImageAdapter adapter;
    private ArrayList<GalleryRequest> imageArrayList;
    TextView teksKosong;


    public galeriLukaPasien() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment galeriLukaPasien.
     */
    // TODO: Rename and change types and number of parameters
    public static galeriLukaPasien newInstance(String param1, String param2) {
        galeriLukaPasien fragment = new galeriLukaPasien();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        NRM = getArguments().getString("NRM");
        getImageURL(NRM);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        NRM = getArguments().getString("NRM");
        View inf = inflater.inflate(R.layout.fragment_galeri_luka_pasien, container, false);
        recyclerView = (RecyclerView) inf.findViewById(R.id.rv_images);
        chipRaw = (Chip) inf.findViewById(R.id.chipRaw);
        chipTepi = (Chip) inf.findViewById(R.id.chipTepi);
        chipDiameter = (Chip) inf.findViewById(R.id.chipDiameter);
        chipAll = (Chip) inf.findViewById(R.id.chipAll);
        teksKosong = (TextView) inf.findViewById(R.id.teksKosong);

        getImageURL(NRM);

        chipAll.setChecked(true);
        chipTepi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter("Anotasi Tepi", "Jpg");
            }
        });

        chipDiameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter("Anotasi Diameter", "Jpg");
            }
        });

        chipAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType("Jpg");
            }
        });

        chipRaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){
                    filter("Raw", "Jpg");
                }
            }
        });

        return inf;


    }

    private void filter(String text, String type){
        // creating a new array list to filter our data.
        ArrayList<GalleryRequest> filteredlist = new ArrayList<GalleryRequest>();

        // running a for loop to compare elements.
        for (GalleryRequest item : imageArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getCategory() == null || item.getType() == null){System.out.println("Null");}else{
                if (item.getCategory().toLowerCase().contains(text.toLowerCase()) && item.getType().toLowerCase().contains(type.toLowerCase())) {
                    // if the item is matched we are
                    // adding it to our filtered list.
                    filteredlist.add(item);
                }
            }

        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    private void filterType(String type){
        // creating a new array list to filter our data.
        ArrayList<GalleryRequest> filteredlist = new ArrayList<GalleryRequest>();

        if(imageArrayList.isEmpty()){
            teksKosong.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        } else {

            // running a for loop to compare elements.
            for (GalleryRequest item : imageArrayList) {
                // checking if the entered string matched with any item of our recycler view.
                if (item.getCategory() == null || item.getType() == null) {
                    System.out.println("Null");
                } else {
                    if (item.getType().toLowerCase().contains(type.toLowerCase())) {
                        // if the item is matched we are
                        // adding it to our filtered list.
                        filteredlist.add(item);
                    }
                }

            }
            if (filteredlist.isEmpty()) {
                // if no item is added in filtered list we are
                // displaying a toast message as no data found.
                Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
            } else {
                // at last we are passing that filtered
                // list to our adapter class.
                adapter.filterList(filteredlist);
            }
        }
    }

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
                        adapter = new ImageAdapter(imageArrayList, com.example.chronicwound.galeriLukaPasien.this);

                        int numberOfColumns = 3;

                        adapter = new com.example.chronicwound.gallery.ImageAdapter(imageArrayList, com.example.chronicwound.galeriLukaPasien.this);

                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns);

                        recyclerView.setLayoutManager(layoutManager);

                        recyclerView.setAdapter(adapter);
                    }

                    filterType("Jpg");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GalleryRequest>> call, Throwable t) {
                // in the method of on failure we are displaying a
                // toast message for fail to get data.
                Toast.makeText(getContext(), "Fail to get data", Toast.LENGTH_SHORT).show();
            }
        });


    }
}