package com.policycenter.model;

public class AccountLocation {
    private String publicID;
    private int locationNum;
    private String locationName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String county;
    private String territoryCode;
    private String geocodeStatus;

    public AccountLocation() {}

    public AccountLocation(String publicID, int locationNum, String locationName, String addressLine1, String city, String state, String postalCode) {
        this.publicID = publicID;
        this.locationNum = locationNum;
        this.locationName = locationName;
        this.addressLine1 = addressLine1;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.geocodeStatus = "ExactMatch";
        this.territoryCode = state + "-01";
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public int getLocationNum() { return locationNum; }
    public void setLocationNum(int locationNum) { this.locationNum = locationNum; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCounty() { return county; }
    public void setCounty(String county) { this.county = county; }

    public String getTerritoryCode() { return territoryCode; }
    public void setTerritoryCode(String territoryCode) { this.territoryCode = territoryCode; }

    public String getGeocodeStatus() { return geocodeStatus; }
    public void setGeocodeStatus(String geocodeStatus) { this.geocodeStatus = geocodeStatus; }
}
