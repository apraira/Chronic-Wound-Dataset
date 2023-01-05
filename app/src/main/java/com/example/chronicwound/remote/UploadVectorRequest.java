package com.example.chronicwound.remote;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

public class UploadVectorRequest {

    private String paths,  id_pasien, id_perawat;
    private String category;

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }

    public String getId_pasien() {
        return id_pasien;
    }

    public void setId_pasien(String id) {
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
