package com.policycenter.model;

public class PolicyAddlInsured {
    private String publicID;
    private String contactID;
    private String interestType; // Mortgagee, LossPayee, CertificateHolder, AdditionalInsured
    private boolean certificateRequired;

    public PolicyAddlInsured() {}

    public PolicyAddlInsured(String publicID, String contactID, String interestType) {
        this.publicID = publicID;
        this.contactID = contactID;
        this.interestType = interestType;
        this.certificateRequired = true;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getContactID() { return contactID; }
    public void setContactID(String contactID) { this.contactID = contactID; }

    public String getInterestType() { return interestType; }
    public void setInterestType(String interestType) { this.interestType = interestType; }

    public boolean isCertificateRequired() { return certificateRequired; }
    public void setCertificateRequired(boolean certificateRequired) { this.certificateRequired = certificateRequired; }
}
