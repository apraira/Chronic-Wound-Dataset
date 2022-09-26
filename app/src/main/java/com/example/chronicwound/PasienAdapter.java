package com.example.chronicwound;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.chronicwound.remote.PasienRequest;

import java.util.ArrayList;

/**
 * Created by Dimas Maulana on 5/26/17.
 * Email : araymaulana66@gmail.com
 */

public class PasienAdapter extends RecyclerView.Adapter<PasienAdapter.MahasiswaViewHolder> {
    private AdapterView.OnItemClickListener listener;
    private String KEY_NAME = "NRM";

    private ArrayList<PasienRequest> dataList;

    public PasienAdapter(ArrayList<PasienRequest> dataList, listPasienActivity listPasienActivity) {

        this.dataList = dataList;
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
        holder.txtNRM.setText("NRM: " + dataList.get(position).getNrm());
        holder.txtUsia.setText(dataList.get(position).getUsia() + " Tahun");
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class MahasiswaViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtNRM, txtUsia;

        public MahasiswaViewHolder(View itemView) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.txt_nama_Pasien);
            txtNRM = (TextView) itemView.findViewById(R.id.txt_nrm_pasien);
            txtUsia = (TextView) itemView.findViewById(R.id.txt_skor_terakhir);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String NRM =  dataList.get(position).getNrm();
                    Context context = v.getContext();
                    //Snackbar.make(itemView, dataList.get(position).getNrm(), Snackbar.LENGTH_LONG).show();
                    Intent i = new Intent(context, detailPasienActivity.class);
                    i.putExtra(KEY_NAME, NRM);
                    context.startActivity(i);
                }
            });
        }



        }



    }
