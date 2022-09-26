package com.example.chronicwound;

public class model {
    private String nama;
    private String npm;
    private String nohp;

    public model(String nama, String npm, String nohp) {
        this.nama = nama;
        this.npm = npm;
        this.nohp = nohp;

    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNpm() {
        return "Asessment Terakhir: " + npm;
    }

    public void setNpm(String npm) {
        this.npm = "Asessment Terakhir: "+ npm;
    }

    public String getNohp() {
        return "Skor Terakhir: " + nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = "Skor Terakhir: " + nohp;
    }
}
