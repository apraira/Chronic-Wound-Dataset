package com.example.chronicwound.remote;

public class AnotasiDiameterRequest {
    private String paths, paths2, paths3,  id_pasien, id_perawat;
    private String category;

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }

    public String getPaths2() {
        return paths2;
    }

    public void setPaths2(String paths2) {
        this.paths2 = paths2;
    }

    public String getPaths3() {
        return paths3;
    }

    public void setPaths3(String paths3) {
        this.paths3 = paths3;
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
