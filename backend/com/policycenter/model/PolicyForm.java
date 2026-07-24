package com.policycenter.model;

public class PolicyForm {
    private String publicID;
    private String formNumber;
    private String formName;
    private String edition;
    private String inferenceRule;

    public PolicyForm() {}

    public PolicyForm(String publicID, String formNumber, String formName, String edition, String inferenceRule) {
        this.publicID = publicID;
        this.formNumber = formNumber;
        this.formName = formName;
        this.edition = edition;
        this.inferenceRule = inferenceRule;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getFormNumber() { return formNumber; }
    public void setFormNumber(String formNumber) { this.formNumber = formNumber; }

    public String getFormName() { return formName; }
    public void setFormName(String formName) { this.formName = formName; }

    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }

    public String getInferenceRule() { return inferenceRule; }
    public void setInferenceRule(String inferenceRule) { this.inferenceRule = inferenceRule; }
}
