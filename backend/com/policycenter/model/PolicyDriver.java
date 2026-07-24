package com.policycenter.model;

public class PolicyDriver {
    private String publicID;
    private int driverNum;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String licenseNumber;
    private String licenseState;
    private int numberOfViolations;
    private boolean goodDriverDiscount;

    public PolicyDriver() {}

    public PolicyDriver(String publicID, int driverNum, String firstName, String lastName, String licenseNumber, String licenseState) {
        this.publicID = publicID;
        this.driverNum = driverNum;
        this.firstName = firstName;
        this.lastName = lastName;
        this.licenseNumber = licenseNumber;
        this.licenseState = licenseState;
        this.dateOfBirth = "1985-05-15";
        this.numberOfViolations = 0;
        this.goodDriverDiscount = true;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public int getDriverNum() { return driverNum; }
    public void setDriverNum(int driverNum) { this.driverNum = driverNum; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getLicenseState() { return licenseState; }
    public void setLicenseState(String licenseState) { this.licenseState = licenseState; }

    public int getNumberOfViolations() { return numberOfViolations; }
    public void setNumberOfViolations(int numberOfViolations) { this.numberOfViolations = numberOfViolations; }

    public boolean isGoodDriverDiscount() { return goodDriverDiscount; }
    public void setGoodDriverDiscount(boolean goodDriverDiscount) { this.goodDriverDiscount = goodDriverDiscount; }
}
