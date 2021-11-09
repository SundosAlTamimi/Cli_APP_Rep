package com.example.valetappsec.Model;

public class ValetFireBaseItem {
    String Status;
    String ifReturn;
    String rawIdActivate;
    String LatLocation;
    String LongLocation;
    String userId;
    String captainName;
    String captainId;
    String captainPhoneNo;
    String clientName;
    String clientId;
    String clientPhoneNo;
    public ValetFireBaseItem(String status, String ifReturn, String rawIdActivate, String latLocation, String longLocation, String userId) {
        Status = status;
        this.ifReturn = ifReturn;
        this.rawIdActivate = rawIdActivate;
        LatLocation = latLocation;
        LongLocation = longLocation;
        this.userId = userId;
    }

    public ValetFireBaseItem() {
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getIfReturn() {
        return ifReturn;
    }

    public void setIfReturn(String ifReturn) {
        this.ifReturn = ifReturn;
    }

    public String getRawIdActivate() {
        return rawIdActivate;
    }

    public void setRawIdActivate(String rawIdActivate) {
        this.rawIdActivate = rawIdActivate;
    }

    public String getLatLocation() {
        return LatLocation;
    }

    public void setLatLocation(String latLocation) {
        LatLocation = latLocation;
    }

    public String getLongLocation() {
        return LongLocation;
    }

    public void setLongLocation(String longLocation) {
        LongLocation = longLocation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public String getCaptainId() {
        return captainId;
    }

    public void setCaptainId(String captainId) {
        this.captainId = captainId;
    }

    public String getCaptainPhoneNo() {
        return captainPhoneNo;
    }

    public void setCaptainPhoneNo(String captainPhoneNo) {
        this.captainPhoneNo = captainPhoneNo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientPhoneNo() {
        return clientPhoneNo;
    }

    public void setClientPhoneNo(String clientPhoneNo) {
        this.clientPhoneNo = clientPhoneNo;
    }
}
