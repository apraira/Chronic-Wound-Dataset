package com.example.chronicwound.remote;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class uploadImageUser {

    private MultipartBody.Part image;
    private RequestBody id_perawat;

    public void setImage(MultipartBody.Part image) {
        this.image = image;
    }

    public MultipartBody.Part getImage() {
        return image;
    }

    public void setId_perawat(RequestBody id_perawat) {
        this.id_perawat = id_perawat;
    }

    public RequestBody getId_perawat() {
        return id_perawat;
    }
}
