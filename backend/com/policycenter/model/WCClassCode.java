package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_wcclasscode")
public class WCClassCode {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "code")
    private String code;

    @Column(name = "state")
    private String state;

    @Column(name = "short_desc")
    private String shortDesc;

    @Column(name = "base_rate")
    private double baseRate;

    public WCClassCode() {}

    public WCClassCode(String publicID, String code, String state, String shortDesc, double baseRate) {
        this.publicID = publicID;
        this.code = code;
        this.state = state;
        this.shortDesc = shortDesc;
        this.baseRate = baseRate;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getShortDesc() { return shortDesc; }
    public void setShortDesc(String shortDesc) { this.shortDesc = shortDesc; }

    public double getBaseRate() { return baseRate; }
    public void setBaseRate(double baseRate) { this.baseRate = baseRate; }
}
