package com.example.valetappsec.Model;

public class ColorCarModel {
    String carColorName;
    String carColorNo;

    public ColorCarModel() {
    }

    public ColorCarModel(String carColorName, String carColorNo) {
        this.carColorName = carColorName;
        this.carColorNo = carColorNo;
    }

    public String getCarColorName() {
        return carColorName;
    }

    public void setCarColorName(String carColorName) {
        this.carColorName = carColorName;
    }

    public String getCarColorNo() {
        return carColorNo;
    }

    public void setCarColorNo(String carColorNo) {
        this.carColorNo = carColorNo;
    }
}
