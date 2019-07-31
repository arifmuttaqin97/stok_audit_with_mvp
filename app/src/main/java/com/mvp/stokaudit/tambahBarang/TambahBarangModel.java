package com.mvp.stokaudit.tambahBarang;

import java.util.ArrayList;
import java.util.HashMap;

class TambahBarangModel {
    String id_barang, id_lokasi, serial;
    ArrayList<HashMap<String, Integer>> data_image;

    public TambahBarangModel(String id_barang, String id_lokasi, String serial, ArrayList<HashMap<String, Integer>> data_image) {
        this.id_barang = id_barang;
        this.id_lokasi = id_lokasi;
        this.serial = serial;
        this.data_image = data_image;
    }


    //    public String getId_barang() {
//        return id_barang;
//    }
//
//    public void setId_barang(String id_barang) {
//        this.id_barang = id_barang;
//    }
//
//    public String getId_lokasi() {
//        return id_lokasi;
//    }
//
//    public void setId_lokasi(String id_lokasi) {
//        this.id_lokasi = id_lokasi;
//    }
//
//    public String getNama_barang() {
//        return nama_barang;
//    }
//
//    public void setNama_barang(String nama_barang) {
//        this.nama_barang = nama_barang;
//    }
//
//    public String getTotal_serial() {
//        return total_serial;
//    }
//
//    public void setTotal_serial(String total_serial) {
//        this.total_serial = total_serial;
//    }
}
