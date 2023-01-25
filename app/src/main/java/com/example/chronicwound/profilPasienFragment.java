package com.example.chronicwound;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.pasien.detailPasienActivity;
import com.example.chronicwound.pasien.editPasien;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profilPasienFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profilPasienFragment extends Fragment {

    TextView tanggalPendaftaran, nomorRegistrasi, namaPasien, tanggalLahir, usiaPasien, jenisKelamin, agamaPasien, emailPasien, nomorHp, alamatPasien;
    private String NRM, id_perawat;
    ImageButton editButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profilPasienFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profilPasienFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profilPasienFragment newInstance(String param1, String param2) {
        profilPasienFragment fragment = new profilPasienFragment();
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
        View inf = inflater.inflate(R.layout.fragment_profil_pasien, container, false);

        tanggalPendaftaran = (TextView) inf.findViewById(R.id.textCreatedAt);
        nomorRegistrasi = (TextView) inf.findViewById(R.id.textNoReg);
        namaPasien = (TextView) inf.findViewById(R.id.textNamaPasien);
        tanggalLahir = (TextView) inf.findViewById(R.id.textTanggalLahir);
        usiaPasien = (TextView) inf.findViewById(R.id.textUsia);
        jenisKelamin = (TextView) inf.findViewById(R.id.textJenisKelamin);
        agamaPasien = (TextView) inf.findViewById(R.id.textAgama);
        emailPasien = (TextView) inf.findViewById(R.id.textEmail);
        nomorHp = (TextView) inf.findViewById(R.id.textNoHp);
        alamatPasien = (TextView) inf.findViewById(R.id.textAlamat);
        editButton = (ImageButton) inf.findViewById(R.id.editButton);

        System.out.println("fragment pasien");

        cariPasien(NRM);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), editPasien.class);

                //formality
                i.putExtra("NRM", NRM);


                //send data
                i.putExtra("textNoReg", String.valueOf(nomorRegistrasi.getText()));
                startActivity(i);
                getActivity().finish();
            }
        });



        return inf;


    }


    // cari pasien
    public void cariPasien(final String nrm) {
        Call<PasienResponse> pasienResponseCall = RetrofitClient.getService().cariPasienNRM(nrm);
        pasienResponseCall.enqueue(new Callback<PasienResponse>() {
            @Override
            public void onResponse(Call<PasienResponse> call, Response<PasienResponse> response) {

                if (response.isSuccessful()) {
                    //login start main activity
                    tanggalPendaftaran.setText(response.body().getCreated_at());
                    nomorRegistrasi.setText(response.body().get_id());
                    namaPasien.setText(response.body().getNama());
                    tanggalLahir.setText(response.body().getBorn_date());
                    usiaPasien.setText(response.body().getUsia() + " Tahun");
                    jenisKelamin.setText(response.body().getKelamin());
                    agamaPasien.setText(response.body().getAgama());
                    emailPasien.setText(response.body().getEmail());
                    nomorHp.setText(response.body().getNo_hp());
                    alamatPasien.setText(response.body().getAlamat());



                } else {
                    Toast.makeText(getContext(), "gagal", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<PasienResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //end of cari pasien
}