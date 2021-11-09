package com.example.valetappsec.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClientOrder {
    //String captainName,String captainPhoneNo,String captainId,String TransferId,
    //            String status ,String captainRate,String currentLocation,String clientName ,String clientNo,String date, String timeToArraive

//[{"id":1012,"CaptainName":"rawan","CaptainPhoneNo":" 9621111","CaptainId":"10","TransferId":"2014","CaptainRate":"3",
// "CurrentLocation":"3","ClientName":"rawan",
// "ClientPhoneNo":" 962786812709","ClientId":"1","Date":"9/28/2021 4:20:23 AM","TimeToArraive":"1111","Status":"0"}]

    @SerializedName("id")
    private int id;
    @SerializedName("CaptainName")
    private String CaptainName;
    @SerializedName("CaptainPhoneNo")
    private String CaptainPhoneNo;
    @SerializedName("CaptainId")
    private int CaptainId;
     @SerializedName("TransferId")
    private int TransferId;
     @SerializedName("CaptainRate")
    private String CaptainRate;
     @SerializedName("CurrentLocation")
    private String CurrentLocation;
     @SerializedName("ClientName")
    private String ClientName;
     @SerializedName("ClientPhoneNo")
    private String ClientPhoneNo;
     @SerializedName("ClientId")
    private int ClientId;
     @SerializedName("Date")
    private String Date;
     @SerializedName("TimeToArraive")
    private String TimeToArraive;
    @SerializedName("Status")
    private String Status;
     private List<ClientOrder> clientOrders;

    public ClientOrder() {

    }

    public ClientOrder(int id, String captainName, String captainPhoneNo, int captainId, int transferId, String captainRate, String currentLocation, String clientName, String clientPhoneNo, int clientId, String date, String timeToArraive, String status, List<ClientOrder> clientOrders) {
        this.id = id;
        CaptainName = captainName;
        CaptainPhoneNo = captainPhoneNo;
        CaptainId = captainId;
        TransferId = transferId;
        CaptainRate = captainRate;
        CurrentLocation = currentLocation;
        ClientName = clientName;
        ClientPhoneNo = clientPhoneNo;
        ClientId = clientId;
        Date = date;
        TimeToArraive = timeToArraive;
        Status = status;
        this.clientOrders = clientOrders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaptainName() {
        return CaptainName;
    }

    public void setCaptainName(String captainName) {
        CaptainName = captainName;
    }

    public String getCaptainPhoneNo() {
        return CaptainPhoneNo;
    }

    public void setCaptainPhoneNo(String captainPhoneNo) {
        CaptainPhoneNo = captainPhoneNo;
    }

    public int getCaptainId() {
        return CaptainId;
    }

    public void setCaptainId(int captainId) {
        CaptainId = captainId;
    }

    public int getTransferId() {
        return TransferId;
    }

    public void setTransferId(int transferId) {
        TransferId = transferId;
    }

    public String getCaptainRate() {
        return CaptainRate;
    }

    public void setCaptainRate(String captainRate) {
        CaptainRate = captainRate;
    }

    public String getCurrentLocation() {
        return CurrentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        CurrentLocation = currentLocation;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientPhoneNo() {
        return ClientPhoneNo;
    }

    public void setClientPhoneNo(String clientPhoneNo) {
        ClientPhoneNo = clientPhoneNo;
    }

    public int getClientId() {
        return ClientId;
    }

    public void setClientId(int clientId) {
        ClientId = clientId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTimeToArraive() {
        return TimeToArraive;
    }

    public void setTimeToArraive(String timeToArraive) {
        TimeToArraive = timeToArraive;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<ClientOrder> getClientOrders() {
        return clientOrders;
    }

    public void setClientOrders(List<ClientOrder> clientOrders) {
        this.clientOrders = clientOrders;
    }
}
