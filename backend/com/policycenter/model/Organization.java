package com.policycenter.model;

/**
 * Guidewire PolicyCenter OOTB Entity: Organization
 *
 * Represents a carrier, agency, MGA, or brokerage in the producer hierarchy.
 * Organizations own ProducerCodes and define the distribution channel structure.
 * The organization tree is used for commission routing, reporting, and UW authority.
 */
public class Organization {
    private String publicId;
    private String name;
    private String orgType;           // Carrier, Agency, MGA, Brokerage
    private String parentOrgId;       // Parent in hierarchy (null for root)
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String phone;
    private String fein;
    private String status;            // Active, Inactive, Suspended
    private String licenseNumber;

    public Organization() {}

    public Organization(String publicId, String name, String orgType, String status) {
        this.publicId = publicId;
        this.name = name;
        this.orgType = orgType;
        this.status = status;
    }

    // --- Getters & Setters ---
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getOrgType() { return orgType; }
    public void setOrgType(String orgType) { this.orgType = orgType; }
    public String getParentOrgId() { return parentOrgId; }
    public void setParentOrgId(String parentOrgId) { this.parentOrgId = parentOrgId; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getFein() { return fein; }
    public void setFein(String fein) { this.fein = fein; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
}
