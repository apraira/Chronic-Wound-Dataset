package com.example.chronicwound.tambahkajian;

import java.util.ArrayList;
import java.util.List;

public class dataKajianResponse {

    private String _id, id_pasien, id_perawat;
    private String size, edges, necrotic_type, necrotic_amount, skincolor_surround, granulation, epithelization, raw_photo_id;

    public String getID() {
        return _id;
    }

    public void setID(String id) {
        this._id = _id;
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




    public dataKajianResponse(String _id, String id_pasien, String id_perawat, String size, String edges, String category, String created_at, String updated_at) {
        this._id = _id;
        this.id_pasien = id_pasien;
        this.id_perawat = id_perawat;
    }
}

