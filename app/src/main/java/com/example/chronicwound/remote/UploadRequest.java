package com.example.chronicwound.remote;

import retrofit2.http.Part;

public class UploadRequest {
    private Part image;
    private String id_pasien, id_perawat, category;

    public Part getImage() {
        return image;
    }

    public void setImage(Part image) {
        this.image = image;
    }


    public String getId_pasien() {
        return id_pasien;
    }

    public void setId_pasien(String password) {
        this.id_pasien = id_pasien;
    }

    public String getId_perawat() {
        return id_perawat;
    }

    public void setId_perawat(String id_perawat) {
        this.id_perawat = id_perawat;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}