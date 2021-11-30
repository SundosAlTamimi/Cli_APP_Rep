package com.example.valetappsec.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarType {
    @SerializedName("brand")
    String carType;
    @SerializedName("models")
    List<String>carModel;

    public CarType() {
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public List<String> getCarModel() {
        return carModel;
    }

    public void setCarModel(List<String> carModel) {
        this.carModel = carModel;
    }



}
