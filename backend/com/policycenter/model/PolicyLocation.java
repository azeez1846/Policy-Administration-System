package com.policycenter.model;

import java.util.ArrayList;
import java.util.List;

public class PolicyLocation {
    private String publicID;
    private int locationNum;
    private String locationName;
    private String addressLine1;
    private String city;
    private String state;
    private String postalCode;
    private int buildingCount;
    private String fireProtectionClass;
    private String taxLocationCode;
    private List<Building> buildings = new ArrayList<>();

    public PolicyLocation() {}

    public PolicyLocation(String publicID, int locationNum, String locationName, String addressLine1, String city, String state, String postalCode) {
        this.publicID = publicID;
        this.locationNum = locationNum;
        this.locationName = locationName;
        this.addressLine1 = addressLine1;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.buildingCount = 0;
        this.fireProtectionClass = "Class 3";
        this.taxLocationCode = state + "-COUNTY-TAX";
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public int getLocationNum() { return locationNum; }
    public void setLocationNum(int locationNum) { this.locationNum = locationNum; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public int getBuildingCount() { return buildingCount; }
    public void setBuildingCount(int buildingCount) { this.buildingCount = buildingCount; }

    public String getFireProtectionClass() { return fireProtectionClass; }
    public void setFireProtectionClass(String fireProtectionClass) { this.fireProtectionClass = fireProtectionClass; }

    public String getTaxLocationCode() { return taxLocationCode; }
    public void setTaxLocationCode(String taxLocationCode) { this.taxLocationCode = taxLocationCode; }

    public List<Building> getBuildings() { return buildings; }
    public void addBuilding(Building building) {
        this.buildings.add(building);
        this.buildingCount = this.buildings.size();
    }
}
