package com.policycenter.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pc_account")
public class Account {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_status")
    private String accountStatus;

    @Transient
    private Contact accountHolder;

    @Column(name = "industry_code")
    private String industryCode;

    @Column(name = "origination_date")
    private String originationDate;

    @Column(name = "preferred_coverage_currency")
    private String preferredCoverageCurrency;

    @Column(name = "frozen")
    private boolean frozen;

    @Transient
    private List<String> policyNumbers = new ArrayList<>();

    @Transient
    private List<AccountLocation> locations = new ArrayList<>();

    @Transient
    private List<AccountContact> contacts = new ArrayList<>();

    public Account() {
        this.preferredCoverageCurrency = "USD";
        this.frozen = false;
    }

    public Account(String publicID, String accountNumber, Contact accountHolder, String industryCode) {
        this.publicID = publicID;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.industryCode = industryCode;
        this.accountStatus = "Active";
        this.originationDate = java.time.LocalDate.now().toString();
        this.preferredCoverageCurrency = "USD";
        this.frozen = false;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }

    public Contact getAccountHolder() { return accountHolder; }
    public void setAccountHolder(Contact accountHolder) { this.accountHolder = accountHolder; }

    public String getIndustryCode() { return industryCode; }
    public void setIndustryCode(String industryCode) { this.industryCode = industryCode; }

    public String getOriginationDate() { return originationDate; }
    public void setOriginationDate(String originationDate) { this.originationDate = originationDate; }

    public String getPreferredCoverageCurrency() { return preferredCoverageCurrency; }
    public void setPreferredCoverageCurrency(String preferredCoverageCurrency) { this.preferredCoverageCurrency = preferredCoverageCurrency; }

    public boolean isFrozen() { return frozen; }
    public void setFrozen(boolean frozen) { this.frozen = frozen; }

    public List<String> getPolicyNumbers() { return policyNumbers; }
    public void setPolicyNumbers(List<String> policyNumbers) { this.policyNumbers = policyNumbers; }

    public void addPolicyNumber(String polNum) {
        if (!this.policyNumbers.contains(polNum)) {
            this.policyNumbers.add(polNum);
        }
    }

    public List<AccountLocation> getLocations() { return locations; }
    public void setLocations(List<AccountLocation> locations) { this.locations = locations; }

    public List<AccountContact> getContacts() { return contacts; }
    public void setContacts(List<AccountContact> contacts) { this.contacts = contacts; }
}
