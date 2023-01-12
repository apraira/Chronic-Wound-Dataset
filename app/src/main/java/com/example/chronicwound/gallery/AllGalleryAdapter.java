package com.example.chronicwound.gallery;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ahmadrosid.svgloader.SvgLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.chronicwound.LoginActivity;
import com.example.chronicwound.R;
import com.example.chronicwound.galeriLukaPasien;
import com.example.chronicwound.pasien.detailPasienActivity;
import com.example.chronicwound.remote.PasienResponse;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dimas Maulana on 5/26/17.
 * Email : araymaulana66@gmail.com
 */

public class AllGalleryAdapter extends RecyclerView.Adapter<com.example.chronicwound.gallery.AllGalleryAdapter.ImageViewHolder> {
    private AdapterView.OnItemClickListener listener;
    private String KEY_NAME = "NRM"; //nomor registrasi pasien
    private String id_image, filename;
    galeriLukaPasien myFragment;


    private ArrayList<GalleryRequest> dataList;

    public AllGalleryAdapter(ArrayList<GalleryRequest> dataList, GaleriActivity GaleriActivity) {

        this.dataList = dataList;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<GalleryRequest> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        dataList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @Override
    public com.example.chronicwound.gallery.AllGalleryAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.galeri_card, parent, false);
        return new com.example.chronicwound.gallery.AllGalleryAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(com.example.chronicwound.gallery.AllGalleryAdapter.ImageViewHolder holder, int position) {
        final GalleryRequest imageModel = (com.example.chronicwound.gallery.GalleryRequest) dataList.get(position);
        String type = imageModel.getType();

        if ("Vector".equals(type)) {
            System.out.println(type + "Vector Image Detected is not loaded by glide");
            /*
            SvgLoader.pluck()
                    .with((Activity) holder.itemView.getContext())
                    .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                    .load("https://jft.web.id/woundapi/instance/uploads/" + imageModel.getFilename(), holder.imgView);*/
        } else {
            System.out.println(type + " is loaded by Glide");
            Glide.with(holder.itemView.getContext()).load("https://jft.web.id/woundapi/instance/uploads/" + imageModel.getFilename())
                    .centerCrop()
                    .thumbnail(0.05f)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imgView);
        }

        //Picasso.get().load("https://jft.web.id/woundapi/instance/uploads/" + imageModel.getFilename()).resize(200,200).centerCrop().into(holder.imgView);
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
                    String IDPasien = dataList.get(position).getId_pasien();
                    Context context = v.getContext();
                    Intent i = new Intent(context, singleImageView.class);
                    i.putExtra("IDPasien", IDPasien);
                    i.putExtra(KEY_NAME, IDImage);
                    context.startActivity(i);
                }
            });
        }
    }



}




