package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_document")
public class Document {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "job_id")
    private String jobID;

    @Column(name = "name")
    private String name;

    @Column(name = "doc_type")
    private String docType;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "status")
    private String status;

    @Column(name = "url")
    private String url;

    @Column(name = "date_created")
    private String dateCreated;

    public Document() {}

    public Document(String publicID, String accountNumber, String jobID, String name, String docType, String mimeType, String status, String url, String dateCreated) {
        this.publicID = publicID;
        this.accountNumber = accountNumber;
        this.jobID = jobID;
        this.name = name;
        this.docType = docType;
        this.mimeType = mimeType;
        this.status = status;
        this.url = url;
        this.dateCreated = dateCreated;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getJobID() { return jobID; }
    public void setJobID(String jobID) { this.jobID = jobID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDocType() { return docType; }
    public void setDocType(String docType) { this.docType = docType; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getDateCreated() { return dateCreated; }
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }
}
