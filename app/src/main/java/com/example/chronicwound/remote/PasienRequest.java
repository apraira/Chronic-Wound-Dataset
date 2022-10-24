package com.example.chronicwound.remote;

public class PasienRequest {
    private String _id;
    private String id_perawat;
    private String nama, agama, born_date, usia, kelamin, alamat, no_hp, email;

    public PasienRequest(String _id, String id_perawat, String nama,  String agama, String born_date,
                          String usia, String kelamin, String alamat, String no_hp, String email) {
        this._id = _id;
        this.id_perawat = id_perawat;
        this.nama = nama;
        this.agama = agama;
        this.born_date = born_date;
        this.usia = usia;
        this.kelamin = kelamin;
        this.alamat = alamat;
        this.no_hp = no_hp;
        this.email = email;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String  getId_perawat() {
        return id_perawat;
    }

    public void setId_perawat(String id_perawat) {
        this.id_perawat = id_perawat;
    }

    public String getNama() {return nama;}

    public void setNama(String nama) {this.nama = nama;}

    public String getAgama() {return agama;}

    public void  setAgama(String agama){this.agama= agama;}

    public String getBorn_date(){return born_date;}

    public void  setBorn_date(String born_date) {this.born_date = born_date;}

    public String getUsia(){return usia;}

    public void setUsia(String usia){this.usia = usia;}

    public String getKelamin(){return kelamin;}

    public void setKelamin(String kelamin){this.kelamin = kelamin;}

    public String getAlamat(){return alamat;}

    public void setAlamat(String alamat){this.alamat = alamat;}

    public String getNo_hp(){return no_hp;}

    public void setNo_hp(String no_hp){this.no_hp = no_hp;}

    public String getEmail(){return email;}

    public void setEmail(String email){this.email = email;}


}
