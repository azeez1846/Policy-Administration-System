package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_glexposure")
public class GLExposure {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "policy_period_id")
    private String policyPeriodID;

    @Column(name = "class_code")
    private String classCode;

    @Column(name = "location_num")
    private int locationNum;

    @Column(name = "exposure_amount")
    private double exposureAmount;

    public GLExposure() {}

    public GLExposure(String publicID, String policyPeriodID, String classCode, int locationNum, double exposureAmount) {
        this.publicID = publicID;
        this.policyPeriodID = policyPeriodID;
        this.classCode = classCode;
        this.locationNum = locationNum;
        this.exposureAmount = exposureAmount;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getPolicyPeriodID() { return policyPeriodID; }
    public void setPolicyPeriodID(String policyPeriodID) { this.policyPeriodID = policyPeriodID; }

    public String getClassCode() { return classCode; }
    public void setClassCode(String classCode) { this.classCode = classCode; }

    public int getLocationNum() { return locationNum; }
    public void setLocationNum(int locationNum) { this.locationNum = locationNum; }

    public double getExposureAmount() { return exposureAmount; }
    public void setExposureAmount(double exposureAmount) { this.exposureAmount = exposureAmount; }
}
