package com.example.chronicwound.tambahpasien;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.chronicwound.R;
import com.example.chronicwound.pasien.detailPasienActivity;
import com.example.chronicwound.pasien.listPasienActivity;
import com.example.chronicwound.remote.PasienRequest;
import com.example.chronicwound.remote.PasienResponse;

import java.util.ArrayList;

import static com.example.chronicwound.MainActivity.id_nurse;
import static com.example.chronicwound.logging.LogHelper.InsertLog;


public class PasienAdapter extends RecyclerView.Adapter<PasienAdapter.MahasiswaViewHolder> {
    private AdapterView.OnItemClickListener listener;
    private String KEY_NAME = "NRM";

    private ArrayList<PasienResponse> dataList;

    public PasienAdapter(ArrayList<PasienResponse> dataList, listPasienActivity listPasienActivity) {

        this.dataList = dataList;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<PasienResponse> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        dataList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @Override
    public MahasiswaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_pasien_card, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MahasiswaViewHolder holder, int position) {
        holder.txtNama.setText(dataList.get(position).getNama());
        holder.txtNRM.setText("NRM: " + dataList.get(position).get_id());
        holder.txtKelamin.setText(dataList.get(position).getKelamin());
        holder.txtUsia.setText(dataList.get(position).getUsia() + " Tahun");
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class MahasiswaViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtNRM, txtKelamin, txtUsia;

        public MahasiswaViewHolder(View itemView) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.nama_pasien);
            txtNRM = (TextView) itemView.findViewById(R.id.nomorRekamMedis);
            txtKelamin = (TextView) itemView.findViewById(R.id.jenisKelamin);
            txtUsia = (TextView) itemView.findViewById(R.id.usiaPasien);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InsertLog(id_nurse, "Memasuki halaman detail pasien");
                    int position = getAdapterPosition();
                    String NRM =  dataList.get(position).get_id();
                    String id_perawat = dataList.get(position).getId_perawat();
                    Context context = v.getContext();
                    //Snackbar.make(itemView, dataList.get(position).getNrm(), Snackbar.LENGTH_LONG).show();
                    Intent i = new Intent(context, detailPasienActivity.class);
                    i.putExtra("id_perawat", id_perawat);
                    i.putExtra(KEY_NAME, NRM);
                    System.out.println("list pasien:" +   id_perawat + "," + NRM);
                    context.startActivity(i);
                }
            });
        }



        }



    }
