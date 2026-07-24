package com.policycenter.model;

/**
 * Guidewire Marketplace Accelerator Entity: ESignatureEnvelope
 *
 * Tracks DocuSign e-signature envelope lifecycle, recipient signers, status changes,
 * and auto-attachment of completed signed PDF documents into pc_document.
 */
public class ESignatureEnvelope {
    private String id;                  // Public ID (env-1001)
    private String envelopeId;          // DocuSign UUID
    private String jobNumber;           // Associated Submission / PolicyChange job
    private String policyNumber;
    private String signerName;
    private String signerEmail;
    private String documentType;        // ACORD 125, ACORD 126, Policy Change Endorsement, UM Waiver
    private String status;              // Sent, Delivered, Completed, Declined, Voided
    private String sentAt;
    private String signedAt;
    private String documentId;          // Associated pc_document ID upon completion
    private String downloadUrl;

    public ESignatureEnvelope() {}

    public ESignatureEnvelope(String id, String envelopeId, String jobNumber, String policyNumber,
                              String signerName, String signerEmail, String documentType,
                              String status, String sentAt) {
        this.id = id;
        this.envelopeId = envelopeId;
        this.jobNumber = jobNumber;
        this.policyNumber = policyNumber;
        this.signerName = signerName;
        this.signerEmail = signerEmail;
        this.documentType = documentType;
        this.status = status;
        this.sentAt = sentAt;
    }

    // --- Getters & Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEnvelopeId() { return envelopeId; }
    public void setEnvelopeId(String envelopeId) { this.envelopeId = envelopeId; }
    public String getJobNumber() { return jobNumber; }
    public void setJobNumber(String jobNumber) { this.jobNumber = jobNumber; }
    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    public String getSignerName() { return signerName; }
    public void setSignerName(String signerName) { this.signerName = signerName; }
    public String getSignerEmail() { return signerEmail; }
    public void setSignerEmail(String signerEmail) { this.signerEmail = signerEmail; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSentAt() { return sentAt; }
    public void setSentAt(String sentAt) { this.sentAt = sentAt; }
    public String getSignedAt() { return signedAt; }
    public void setSignedAt(String signedAt) { this.signedAt = signedAt; }
    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
}
