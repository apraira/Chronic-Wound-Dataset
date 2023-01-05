package com.example.chronicwound.tambahkajian;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.KajianLuka.detailKajian;
import com.example.chronicwound.historiKajianFragment;
import com.example.chronicwound.R;
import com.example.chronicwound.pasien.detailPasienActivity;
import com.example.chronicwound.pasien.listPasienActivity;
import com.example.chronicwound.remote.KajianResponse;
import com.example.chronicwound.remote.LoginResponse;
import com.example.chronicwound.remote.PasienRequest;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class kajianAdapter extends RecyclerView.Adapter<kajianAdapter.HistoriViewHolder> {
    private AdapterView.OnItemClickListener listener;
    private String KEY_NAME = "NRM";

    private ArrayList<KajianResponse> dataList;

    public kajianAdapter(ArrayList<KajianResponse> dataList, com.example.chronicwound.historiKajianFragment historiKajianFragment) {

        this.dataList = dataList;
    }

    @Override
    public HistoriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.histori_kajian_card, parent, false);
        return new HistoriViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(HistoriViewHolder holder, int position) {

        String tanggal = dataList.get(position).getCreated_at().replaceAll(" .*", "");

        String startDateString = "08-12-2017";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MMMM/yyyy");
        System.out.println(LocalDate.parse(tanggal, formatter).format(formatter2));

        holder.txtTanggal.setText(LocalDate.parse(tanggal, formatter).format(formatter2).replaceAll("/", " "));


        Call<LoginResponse> loginResponseCall = RetrofitClient.getService().cariIDPerawat(dataList.get(position).getId_perawat());
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    //login start main activity
                    String nama = response.body().getName();
                    String nip = response.body().getUsername();
                    holder.txtPerawat.setText( response.body().getName());
                    holder.txtNIP.setText("NIP: " + response.body().getUsername());


                } else {
                    System.out.println("gagal");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class HistoriViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTanggal, txtPerawat, txtNIP;

        public HistoriViewHolder(View itemView) {
            super(itemView);
            txtTanggal = (TextView) itemView.findViewById(R.id.txtTanggal);
            txtPerawat = (TextView) itemView.findViewById(R.id.NamaPerawat);
            txtNIP = (TextView) itemView.findViewById(R.id.NIPperawat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String id_kajian = dataList.get(position).get_id();

                    Context context = v.getContext();
                    //Snackbar.make(itemView, dataList.get(position).getNrm(), Snackbar.LENGTH_LONG).show();
                    Intent i = new Intent(context, detailKajian.class);
                    i.putExtra("id_kajian", id_kajian);
                    context.startActivity(i);
                }
            });


        }



    }




}
