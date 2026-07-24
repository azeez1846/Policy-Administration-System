package com.policycenter.model;

import java.util.ArrayList;
import java.util.List;

public class PolicyVehicle {
    private String publicID;
    private int vehicleNum;
    private String vin;
    private String make;
    private String model;
    private int year;
    private String useType; // Commercial, Business, Service, Personal
    private double costNew;
    private int garageLocationNum;
    private String licenseState;
    private final List<Coverage> vehicleCoverages = new ArrayList<>();

    public PolicyVehicle() {}

    public PolicyVehicle(String publicID, int vehicleNum, String vin, String make, String model, int year, double costNew) {
        this.publicID = publicID;
        this.vehicleNum = vehicleNum;
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.costNew = costNew;
        this.useType = "Commercial";
        this.garageLocationNum = 1;
        this.licenseState = "IL";
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public int getVehicleNum() { return vehicleNum; }
    public void setVehicleNum(int vehicleNum) { this.vehicleNum = vehicleNum; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getUseType() { return useType; }
    public void setUseType(String useType) { this.useType = useType; }

    public double getCostNew() { return costNew; }
    public void setCostNew(double costNew) { this.costNew = costNew; }

    public int getGarageLocationNum() { return garageLocationNum; }
    public void setGarageLocationNum(int garageLocationNum) { this.garageLocationNum = garageLocationNum; }

    public String getLicenseState() { return licenseState; }
    public void setLicenseState(String licenseState) { this.licenseState = licenseState; }

    public List<Coverage> getVehicleCoverages() { return vehicleCoverages; }
    public void addVehicleCoverage(Coverage cov) { this.vehicleCoverages.add(cov); }
}
