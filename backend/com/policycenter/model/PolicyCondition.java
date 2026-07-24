package com.policycenter.model;

public class PolicyCondition {
    private String publicID;
    private String patternCode;
    private String name;
    private String conditionText;

    public PolicyCondition() {}

    public PolicyCondition(String publicID, String patternCode, String name, String conditionText) {
        this.publicID = publicID;
        this.patternCode = patternCode;
        this.name = name;
        this.conditionText = conditionText;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getPatternCode() { return patternCode; }
    public void setPatternCode(String patternCode) { this.patternCode = patternCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getConditionText() { return conditionText; }
    public void setConditionText(String conditionText) { this.conditionText = conditionText; }
}
