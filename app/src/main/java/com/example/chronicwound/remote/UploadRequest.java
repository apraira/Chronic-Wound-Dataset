package com.example.chronicwound.remote;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;

public class UploadRequest {
    private MultipartBody.Part image;
    private RequestBody id, id_pasien, id_perawat, type, category;



    public MultipartBody.Part getImage() {
        return image;
    }

    public void setImage(MultipartBody.Part image) {
        this.image = image;
    }

    public RequestBody getId() {
        return id;
    }

    public void setId(RequestBody id) {
        this.id = id;
    }


    public RequestBody getId_pasien() {
        return id_pasien;
    }

    public void setId_pasien(RequestBody id_pasien) {
        this.id_pasien = id_pasien;
    }



    public RequestBody getId_perawat() {
        return id_perawat;
    }

    public void setId_perawat(RequestBody id_perawat) {
        this.id_perawat = id_perawat;
    }

    public RequestBody getType() {
        return type;
    }

    public void setType(RequestBody type) {
        this.type = type;
    }

    public RequestBody getCategory() {
        return category;
    }

    public void setCategory(RequestBody category) {
        this.category = category;
    }
}