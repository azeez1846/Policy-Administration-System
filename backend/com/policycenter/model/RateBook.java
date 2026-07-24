package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_ratebook")
public class RateBook {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "book_code")
    private String bookCode;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "book_edition")
    private String bookEdition;

    @Column(name = "status")
    private String status;

    @Column(name = "effective_date")
    private String effectiveDate;

    @Column(name = "policy_line")
    private String policyLine;

    public RateBook() {}

    public RateBook(String publicID, String bookCode, String bookName, String bookEdition, String status, String effectiveDate, String policyLine) {
        this.publicID = publicID;
        this.bookCode = bookCode;
        this.bookName = bookName;
        this.bookEdition = bookEdition;
        this.status = status;
        this.effectiveDate = effectiveDate;
        this.policyLine = policyLine;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getBookCode() { return bookCode; }
    public void setBookCode(String bookCode) { this.bookCode = bookCode; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public String getBookEdition() { return bookEdition; }
    public void setBookEdition(String bookEdition) { this.bookEdition = bookEdition; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }

    public String getPolicyLine() { return policyLine; }
    public void setPolicyLine(String policyLine) { this.policyLine = policyLine; }
}
