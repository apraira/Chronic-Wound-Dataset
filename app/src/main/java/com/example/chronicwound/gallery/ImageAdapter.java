package com.example.chronicwound.gallery;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chronicwound.LoginActivity;
import com.example.chronicwound.R;
import com.example.chronicwound.remote.PasienResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dimas Maulana on 5/26/17.
 * Email : araymaulana66@gmail.com
 */

public class ImageAdapter extends RecyclerView.Adapter<com.example.chronicwound.gallery.ImageAdapter.ImageViewHolder> {
    private AdapterView.OnItemClickListener listener;
    private String KEY_NAME = "NRM"; //nomor registrasi pasien
    private String id_image, filename;

    private ArrayList<GalleryRequest> dataList;

    public ImageAdapter(ArrayList<GalleryRequest> dataList, GaleriActivity galeriActivity) {

        this.dataList = dataList;
    }

    @Override
    public com.example.chronicwound.gallery.ImageAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.galeri_card, parent, false);
        return new com.example.chronicwound.gallery.ImageAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(com.example.chronicwound.gallery.ImageAdapter.ImageViewHolder holder, int position) {
        final GalleryRequest imageModel = (com.example.chronicwound.gallery.GalleryRequest) dataList.get(position);

        Picasso.get().load("https://jft.web.id/woundapi/instance/uploads/" + imageModel.getFilename()).resize(200,200).centerCrop().into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imgView  = (ImageView) itemView.findViewById(R.id.iv_photo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String IDImage =  dataList.get(position).getID();
                    Context context = v.getContext();
                    Intent i = new Intent(context, singleImageView.class);
                    i.putExtra(KEY_NAME, IDImage);
                    context.startActivity(i);
                }
            });
        }
    }



    }




