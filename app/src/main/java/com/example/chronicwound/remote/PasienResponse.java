package com.example.chronicwound.remote;

public class PasienResponse {
    private String _id;
    private Integer id_perawat;
    private String nama, usia, berat, tinggi;

    public String getNrm() {
        return _id;
    }

    public void setNrm(String nrm) {
        this._id = _id;
    }

    public Integer getId_perawat() {
        return id_perawat;
    }

    public void setId_perawat(Integer id_perawat) {
        this.id_perawat = id_perawat;
    }

    public String getUsia() {return usia;}

    public void  setUsia(String usia){this.usia= usia;}

    public String getNama() {return nama;}

    public void setNama(String nama) {this.nama = nama;}

    public String getBerat(){return berat;}

    public void  setBerat(String berat) {this.berat = berat;}

    public String getTinggi(){return tinggi;}

    public void setTinggi(String tinggi){this.tinggi = tinggi;}

}
