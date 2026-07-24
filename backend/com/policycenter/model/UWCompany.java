package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_uwcompany")
public class UWCompany {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "state")
    private String state;

    @Column(name = "status")
    private String status;

    public UWCompany() {}

    public UWCompany(String publicID, String code, String name, String state, String status) {
        this.publicID = publicID;
        this.code = code;
        this.name = name;
        this.state = state;
        this.status = status;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
