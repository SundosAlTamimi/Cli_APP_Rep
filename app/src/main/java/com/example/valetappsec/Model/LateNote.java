package com.example.valetappsec.Model;

public class LateNote {

    private int id;
    private int idCap;
    private  int idClient;
    private int idT;
    private String phoneCaptain;
    private  String phoneNoClient;
    private String time;
    private  String note;

    public LateNote() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCap() {
        return idCap;
    }

    public void setIdCap(int idCap) {
        this.idCap = idCap;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdT() {
        return idT;
    }

    public void setIdT(int idT) {
        this.idT = idT;
    }

    public String getPhoneCaptain() {
        return phoneCaptain;
    }

    public void setPhoneCaptain(String phoneCaptain) {
        this.phoneCaptain = phoneCaptain;
    }

    public String getPhoneNoClient() {
        return phoneNoClient;
    }

    public void setPhoneNoClient(String phoneNoClient) {
        this.phoneNoClient = phoneNoClient;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
