package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_contact")
public class Contact {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "name")
    private String name;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "contact_type")
    private String contactType;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "work_phone")
    private String workPhone;

    @Column(name = "home_phone")
    private String homePhone;

    @Column(name = "fax_number")
    private String faxNumber;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country")
    private String country;

    @Column(name = "tax_id")
    private String taxID;

    public Contact() {
        this.country = "US";
    }

    public Contact(String publicID, String name, String contactType, String email, String phone) {
        this.publicID = publicID;
        this.name = name;
        this.contactType = contactType;
        this.email = email;
        this.phone = phone;
        this.country = "US";
    }

    public Contact(String publicID, String name, String contactType, String email, String phone, String addressLine1, String city, String state, String postalCode) {
        this.publicID = publicID;
        this.name = name;
        this.contactType = contactType;
        this.email = email;
        this.phone = phone;
        this.addressLine1 = addressLine1;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = "US";
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getName() { return name != null ? name : (firstName + " " + lastName).trim(); }
    public void setName(String name) { this.name = name; }

    public String getFirstName() { return firstName != null ? firstName : name; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getCompanyName() { return getName(); }
    public void setCompanyName(String companyName) { this.name = companyName; }

    public String getContactType() { return contactType; }
    public void setContactType(String contactType) { this.contactType = contactType; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getWorkPhone() { return workPhone; }
    public void setWorkPhone(String workPhone) { this.workPhone = workPhone; }

    public String getHomePhone() { return homePhone; }
    public void setHomePhone(String homePhone) { this.homePhone = homePhone; }

    public String getFaxNumber() { return faxNumber; }
    public void setFaxNumber(String faxNumber) { this.faxNumber = faxNumber; }

    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getTaxID() { return taxID; }
    public void setTaxID(String taxID) { this.taxID = taxID; }
}
