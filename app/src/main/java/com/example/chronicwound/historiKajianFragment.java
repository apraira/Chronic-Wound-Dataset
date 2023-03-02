package com.example.chronicwound;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chronicwound.pasien.listPasienActivity;
import com.example.chronicwound.remote.KajianResponse;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.tambahkajian.kajianAdapter;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link historiKajianFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class historiKajianFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private kajianAdapter adapter;
    private ArrayList<KajianResponse> kajianArrayList;
    private Integer dd;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public historiKajianFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment historiKajianFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static historiKajianFragment newInstance(String param1, String param2) {
        historiKajianFragment fragment = new historiKajianFragment();
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

        //mahasiswaArrayList.clear();
        View inf = inflater.inflate(R.layout.fragment_histori_kajian, container, false);
        recyclerView = (RecyclerView) inf.findViewById(R.id.recycler_view);

        // Get value of shared preferences
        // Inflate the layout for this fragment
        String NRM = getArguments().getString("NRM");
        System.out.println("Id perawat shared preferemces: " + NRM.toString());

        getHistoriKajian(NRM);

        adapter = new kajianAdapter(kajianArrayList, com.example.chronicwound.historiKajianFragment.this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        return inf;
    }

    private void getHistoriKajian(final String NRM) {
        Call<ArrayList<KajianResponse>> KajianResponseCall = RetrofitClient.getService().getHistoriPasien(NRM);

        // on below line we are calling method to enqueue and calling
        // all the data from array list.
        KajianResponseCall.enqueue(new Callback<ArrayList<KajianResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<KajianResponse>> call, Response<ArrayList<KajianResponse>> response) {

                if (response.isSuccessful()) {

                    // below line is to add our data from api to our array list.
                    kajianArrayList = response.body();
                    Collections.reverse(kajianArrayList);

                    // below line we are running a loop to add data to our adapter class.
                    for (int i = 0; i < kajianArrayList.size(); i++) {
                        adapter = new kajianAdapter(kajianArrayList, com.example.chronicwound.historiKajianFragment.this);

                        // below line is to set layout manager for our recycler view.
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());

                        // setting layout manager for our recycler view.
                        recyclerView.setLayoutManager(manager);

                        // below line is to set adapter to our recycler view.
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<KajianResponse>> call, Throwable t) {
                // in the method of on failure we are displaying a
                // toast message for fail to get data.
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("ERROR: " + t.toString());
            }
        });


    }


}