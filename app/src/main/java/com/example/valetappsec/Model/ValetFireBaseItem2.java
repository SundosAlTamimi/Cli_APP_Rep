package com.example.valetappsec.Model;

public class ValetFireBaseItem2 {
    String Status;
    String ifReturn;
    String rawIdActivate;
    String LatLocation;
    String LongLocation;
    String userId;
    String captainName;
    String captainId;
    String captainPhoneNo;

    public ValetFireBaseItem2(String status, String ifReturn, String rawIdActivate, String latLocation, String longLocation, String userId) {
        Status = status;
        this.ifReturn = ifReturn;
        this.rawIdActivate = rawIdActivate;
        LatLocation = latLocation;
        LongLocation = longLocation;
        this.userId = userId;
    }

    public ValetFireBaseItem2() {
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

}
