package com.example.chronicwound;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chronicwound.gallery.GaleriActivity;
import com.example.chronicwound.gallery.GalleryRequest;
import com.example.chronicwound.gallery.ImageAdapter;
import com.example.chronicwound.pasien.detailPasienActivity;
import com.example.chronicwound.remote.RetrofitClient;

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
    private String mParam1;
    private String mParam2;
    private String NRM, id_perawat;
    private RecyclerView recyclerView;
    private com.example.chronicwound.gallery.ImageAdapter adapter;
    private ArrayList<GalleryRequest> imageArrayList;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        NRM = getArguments().getString("NRM");
        View inf = inflater.inflate(R.layout.fragment_galeri_luka_pasien, container, false);
        recyclerView = (RecyclerView) inf.findViewById(R.id.rv_images);


        getImageURL(NRM);
        return inf;
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

                        int numberOfColumns = 2;

                        adapter = new com.example.chronicwound.gallery.ImageAdapter(imageArrayList, com.example.chronicwound.galeriLukaPasien.this);

                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns);

                        recyclerView.setLayoutManager(layoutManager);

                        recyclerView.setAdapter(adapter);
                    }
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