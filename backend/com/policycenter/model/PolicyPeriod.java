package com.policycenter.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pc_policyperiod")
public class PolicyPeriod {

    @Id
    @Column(name = "period_id")
    private String periodID;

    @Column(name = "status")
    private String status;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "policy_number")
    private String policyNumber;

    @Column(name = "term_number")
    private int termNumber;

    @Column(name = "quote_number")
    private String quoteNumber;

    @Column(name = "effective_date")
    private String effectiveDate;

    @Column(name = "expiration_date")
    private String expirationDate;

    @Column(name = "cancellation_date")
    private String cancellationDate;

    @Column(name = "written_date")
    private String writtenDate;

    @Column(name = "currency")
    private String currency;

    @Column(name = "total_premium")
    private double totalPremium;

    @Column(name = "tax_and_fees")
    private double taxAndFees;

    @Column(name = "total_cost")
    private double totalCost;

    @Transient
    private Account account;

    @Transient
    private Contact primaryNamedInsured;

    @Transient
    private List<PolicyLine> lines = new ArrayList<>();

    @Transient
    private List<PolicyLocation> locations = new ArrayList<>();

    @Transient
    private List<Building> buildings = new ArrayList<>();

    @Transient
    private List<Cost> costs = new ArrayList<>();

    @Transient
    private List<UWIssue> uwIssues = new ArrayList<>();

    @Transient
    private List<PolicyForm> forms = new ArrayList<>();

    @Transient
    private List<Note> notes = new ArrayList<>();

    public PolicyPeriod() {
        this.status = "Draft";
        this.productCode = "CommercialProperty";
        this.productName = "Commercial Property";
        this.termNumber = 1;
        this.currency = "USD";
    }

    public PolicyPeriod(String periodID, Account account, Contact primaryNamedInsured, String effectiveDate, String expirationDate) {
        this.periodID = periodID;
        this.account = account;
        this.primaryNamedInsured = primaryNamedInsured;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.status = "Draft";
        this.productCode = "CommercialProperty";
        this.productName = "Commercial Property";
        this.termNumber = 1;
        this.currency = "USD";
    }

    public String getPeriodID() { return periodID; }
    public void setPeriodID(String periodID) { this.periodID = periodID; }
    public String getPublicID() { return periodID; }
    public void setPublicID(String publicID) { this.periodID = publicID; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public int getTermNumber() { return termNumber; }
    public void setTermNumber(int termNumber) { this.termNumber = termNumber; }

    public String getQuoteNumber() { return quoteNumber; }
    public void setQuoteNumber(String quoteNumber) { this.quoteNumber = quoteNumber; }

    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }

    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }

    public String getCancellationDate() { return cancellationDate; }
    public void setCancellationDate(String cancellationDate) { this.cancellationDate = cancellationDate; }

    public String getWrittenDate() { return writtenDate; }
    public void setWrittenDate(String writtenDate) { this.writtenDate = writtenDate; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public double getTotalPremium() { return totalPremium; }
    public void setTotalPremium(double totalPremium) { this.totalPremium = totalPremium; }

    public double getTaxAndFees() { return taxAndFees; }
    public void setTaxAndFees(double taxAndFees) { this.taxAndFees = taxAndFees; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public Contact getPrimaryNamedInsured() { return primaryNamedInsured; }
    public void setPrimaryNamedInsured(Contact primaryNamedInsured) { this.primaryNamedInsured = primaryNamedInsured; }

    public List<PolicyLine> getLines() { return lines; }
    public void setLines(List<PolicyLine> lines) { this.lines = lines; }
    public void addLine(PolicyLine line) { this.lines.add(line); }

    public List<PolicyLocation> getLocations() { return locations; }
    public void setLocations(List<PolicyLocation> locations) { this.locations = locations; }
    public void addLocation(PolicyLocation loc) { this.locations.add(loc); }

    public List<Building> getBuildings() { return buildings; }
    public void setBuildings(List<Building> buildings) { this.buildings = buildings; }

    public List<Cost> getCosts() { return costs; }
    public void setCosts(List<Cost> costs) { this.costs = costs; }

    public List<UWIssue> getUwIssues() { return uwIssues; }
    public void setUwIssues(List<UWIssue> uwIssues) { this.uwIssues = uwIssues; }
    public void addUwIssue(UWIssue issue) { this.uwIssues.add(issue); }

    public List<PolicyForm> getForms() { return forms; }
    public void setForms(List<PolicyForm> forms) { this.forms = forms; }
    public void addForm(PolicyForm form) { this.forms.add(form); }

    public List<Note> getNotes() { return notes; }
    public void setNotes(List<Note> notes) { this.notes = notes; }
    public void addNote(Note note) { this.notes.add(note); }
}
