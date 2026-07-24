package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_formpattern")
public class FormPattern {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "form_number")
    private String formNumber;

    @Column(name = "edition")
    private String edition;

    @Column(name = "description")
    private String description;

    @Column(name = "inference_class")
    private String inferenceClass;

    @Column(name = "priority")
    private int priority;

    public FormPattern() {}

    public FormPattern(String publicID, String formNumber, String edition, String description, String inferenceClass, int priority) {
        this.publicID = publicID;
        this.formNumber = formNumber;
        this.edition = edition;
        this.description = description;
        this.inferenceClass = inferenceClass;
        this.priority = priority;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getFormNumber() { return formNumber; }
    public void setFormNumber(String formNumber) { this.formNumber = formNumber; }

    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInferenceClass() { return inferenceClass; }
    public void setInferenceClass(String inferenceClass) { this.inferenceClass = inferenceClass; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
}
