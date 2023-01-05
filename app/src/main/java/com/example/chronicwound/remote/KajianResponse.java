package com.example.chronicwound.remote;

import java.util.ArrayList;
import java.util.List;

public class KajianResponse {

    private String _id, id_pasien, id_perawat;
    private String size, edges, necrotic_type, necrotic_amount, skincolor_surround, granulation, epithelization, raw_photo_id, tepi_image_id,  diameter_image_id, created_at;

    public String get_id() {
        return _id;
    }

    public void set_id(String id) {
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getEdges() {
        return edges;
    }

    public void setEdges(String edges) {
        this.edges = edges;
    }

    public String getNecrotic_type() {
        return necrotic_type;
    }

    public void setNecrotic_type(String necrotic_type) {
        this.necrotic_type = necrotic_type;
    }

    public String getNecrotic_amount() {
        return necrotic_amount;
    }

    public void setNecrotic_amount(String necrotic_amount) {
        this.necrotic_amount = necrotic_amount;
    }

    public String getSkincolor_surround() {
        return skincolor_surround;
    }

    public void setSkincolor_surround(String skincolor_surround) {
        this.skincolor_surround = skincolor_surround;
    }

    public String getGranulation() {
        return granulation;
    }

    public void setGranulation(String granulation) {
        this.granulation = granulation;
    }

    public String getEpithelization() {
        return epithelization;
    }

    public void setEpithelization(String epithelization) {
        this.epithelization = epithelization;
    }

    public String getRaw_photo_id() {
        return raw_photo_id;
    }

    public void setRaw_photo_id(String raw_photo_id) {
        this.raw_photo_id = raw_photo_id;
    }

    public String getTepi_image_id() {
        return tepi_image_id;
    }

    public void setTepi_image_id(String tepi_image_id) {
        this.tepi_image_id = tepi_image_id;
    }

    public String getDiameter_image_id() {
        return diameter_image_id;
    }

    public void setDiameter_image_id(String diameter_image_id) {
        this.diameter_image_id = diameter_image_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}

