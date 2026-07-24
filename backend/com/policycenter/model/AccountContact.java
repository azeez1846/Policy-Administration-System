package com.policycenter.model;

public class AccountContact {
    private String publicID;
    private String accountID;
    private String contactID;
    private String roles; // NamedInsured, AccountingContact, BillingContact, Driver

    public AccountContact() {}

    public AccountContact(String publicID, String accountID, String contactID, String roles) {
        this.publicID = publicID;
        this.accountID = accountID;
        this.contactID = contactID;
        this.roles = roles;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getAccountID() { return accountID; }
    public void setAccountID(String accountID) { this.accountID = accountID; }

    public String getContactID() { return contactID; }
    public void setContactID(String contactID) { this.contactID = contactID; }

    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
}
