package com.example.valetappsec.Model;

public class CaptainClientTransfer {

    private int id;
    private String ClientName;
    private String ClientPhoneNo;
    private int ClientId;
    private String FromLoc;
    private String ToLocation;
    private String LocationName;
    private int PaymentType;
    private String CaptainName;
    private int CaptainId;
    private String CaptainPhoneNo;
    private int Status;
    private String Rate;
    private String TimeOfArraive;
    private String DateOfTranse;
    private String TimeIn;
    private String Timeout;
    private String TimeParkIn;
    private String TimeParkOut;
    private String statusRaw;
    private double payVal;
    private double amountVal;
    private double reVal;
    private String parkingLocation;
    public CaptainClientTransfer() {

    }

    public CaptainClientTransfer(int id, String clientName, String clientPhoneNo, int clientId, String fromLoc,
                                 String toLocation, String locationName, int paymentType, String captainName,
                                 int captainId, String captainPhoneNo, int status, String rate, String timeOfArraive,
                                 String dateOfTranse, String timeIn, String timeout, String timeParkIn, String timeParkOut) {
        this.id = id;
        this.ClientName = clientName;
        this.ClientPhoneNo = clientPhoneNo;
        this.ClientId = clientId;
        this.FromLoc = fromLoc;
        this.ToLocation = toLocation;
        this.LocationName = locationName;
        this.PaymentType = paymentType;
        this.CaptainName = captainName;
        this.CaptainId = captainId;
        this.CaptainPhoneNo = captainPhoneNo;
        this.Status = status;
        this.Rate = rate;
        this.TimeOfArraive = timeOfArraive;
        this.DateOfTranse = dateOfTranse;
        this.TimeIn = timeIn;
        this.Timeout = timeout;
        this.TimeParkIn = timeParkIn;
        this.TimeParkOut = timeParkOut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFromLoc() {
        return FromLoc;
    }

    public void setFromLoc(String fromLoc) {
        FromLoc = fromLoc;
    }

    public String getToLocation() {
        return ToLocation;
    }

    public void setToLocation(String toLocation) {
        ToLocation = toLocation;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public int getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(int paymentType) {
        PaymentType = paymentType;
    }

    public String getCaptainName() {
        return CaptainName;
    }

    public void setCaptainName(String captainName) {
        CaptainName = captainName;
    }

    public int getCaptainId() {
        return CaptainId;
    }

    public void setCaptainId(int captainId) {
        CaptainId = captainId;
    }

    public String getCaptainPhoneNo() {
        return CaptainPhoneNo;
    }

    public void setCaptainPhoneNo(String captainPhoneNo) {
        CaptainPhoneNo = captainPhoneNo;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getTimeOfArraive() {
        return TimeOfArraive;
    }

    public void setTimeOfArraive(String timeOfArraive) {
        TimeOfArraive = timeOfArraive;
    }

    public String getDateOfTranse() {
        return DateOfTranse;
    }

    public void setDateOfTranse(String dateOfTranse) {
        DateOfTranse = dateOfTranse;
    }

    public String getTimeIn() {
        return TimeIn;
    }

    public void setTimeIn(String timeIn) {
        TimeIn = timeIn;
    }

    public String getTimeout() {
        return Timeout;
    }

    public void setTimeout(String timeout) {
        Timeout = timeout;
    }

    public String getTimeParkIn() {
        return TimeParkIn;
    }

    public void setTimeParkIn(String timeParkIn) {
        TimeParkIn = timeParkIn;
    }

    public String getTimeParkOut() {
        return TimeParkOut;
    }

    public void setTimeParkOut(String timeParkOut) {
        TimeParkOut = timeParkOut;
    }

    public String getStatusRaw() {
        return statusRaw;
    }

    public void setStatusRaw(String statusRaw) {
        this.statusRaw = statusRaw;
    }

    public double getPayVal() {
        return payVal;
    }

    public void setPayVal(double payVal) {
        this.payVal = payVal;
    }

    public double getAmountVal() {
        return amountVal;
    }

    public void setAmountVal(double amountVal) {
        this.amountVal = amountVal;
    }

    public double getReVal() {
        return reVal;
    }

    public void setReVal(double reVal) {
        this.reVal = reVal;
    }

    public String getParkingLocation() {
        return parkingLocation;
    }

    public void setParkingLocation(String parkingLocation) {
        this.parkingLocation = parkingLocation;
    }
}
