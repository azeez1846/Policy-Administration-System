package com.policycenter.model;

import java.util.ArrayList;
import java.util.List;

public class PolicyLine {
    private String publicID;
    private String patternCode; // e.g. CommercialPropertyLine, PersonalAutoLine
    private String lineName;
    private final List<Building> buildings = new ArrayList<>();
    private final List<PolicyVehicle> vehicles = new ArrayList<>();
    private final List<PolicyDriver> drivers = new ArrayList<>();
    private final List<Coverage> lineCoverages = new ArrayList<>();

    public PolicyLine() {}

    public PolicyLine(String publicID, String patternCode, String lineName) {
        this.publicID = publicID;
        this.patternCode = patternCode;
        this.lineName = lineName;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getPatternCode() { return patternCode; }
    public void setPatternCode(String patternCode) { this.patternCode = patternCode; }

    public String getLineName() { return lineName; }
    public void setLineName(String lineName) { this.lineName = lineName; }

    public List<Building> getBuildings() { return buildings; }
    public void addBuilding(Building building) { this.buildings.add(building); }

    public List<PolicyVehicle> getVehicles() { return vehicles; }
    public void addVehicle(PolicyVehicle vehicle) { this.vehicles.add(vehicle); }

    public List<PolicyDriver> getDrivers() { return drivers; }
    public void addDriver(PolicyDriver driver) { this.drivers.add(driver); }

    public List<Coverage> getLineCoverages() { return lineCoverages; }
    public void addLineCoverage(Coverage coverage) { this.lineCoverages.add(coverage); }
}
