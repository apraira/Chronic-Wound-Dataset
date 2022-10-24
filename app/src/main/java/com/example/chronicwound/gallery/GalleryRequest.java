package com.example.chronicwound.gallery;

import java.util.ArrayList;
import java.util.List;

public class GalleryRequest {

    private String _id, id_pasien, id_perawat;
    private String filename, filepath, category, created_at, updated_at;

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = _id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this._id = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this._id = updated_at;
    }


    public GalleryRequest(String _id, String id_pasien, String id_perawat, String filename, String filepath, String category, String created_at, String updated_at) {
        this._id = _id;
        this.id_pasien = id_pasien;
        this.id_perawat = id_perawat;
        this.filename = filename;
        this.filepath = filepath;
        this.category = category;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
