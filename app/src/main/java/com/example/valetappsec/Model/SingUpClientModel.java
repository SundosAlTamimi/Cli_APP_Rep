package com.example.valetappsec.Model;

public class SingUpClientModel {

    private String id;
    private  String userName;
    private String phoneNo;
    private String email;
    private String password;

    private String carType;
    private  String carModel;
    private  String CarColor;
    private String CarLot;
    private  String carPic;

    public SingUpClientModel() {

    }

    public SingUpClientModel(String id, String userName, String phoneNo, String email, String password,
                             String carType, String carModel, String carColor, String carLot) {
        this.id = id;
        this.userName = userName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.password = password;
        this.carType = carType;
        this.carModel = carModel;
        CarColor = carColor;
        CarLot = carLot;
    }

    public SingUpClientModel(String userName, String phoneNo, String email, String password, String carType,
                             String carModel, String carColor, String carLot) {
        this.userName = userName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.password = password;
        this.carType = carType;
        this.carModel = carModel;
        this.CarColor = carColor;
        this.CarLot = carLot;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return CarColor;
    }

    public void setCarColor(String carColor) {
        CarColor = carColor;
    }

    public String getCarLot() {
        return CarLot;
    }

    public void setCarLot(String carLot) {
        CarLot = carLot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarPic() {
        return carPic;
    }

    public void setCarPic(String carPic) {
        this.carPic = carPic;
    }
}
