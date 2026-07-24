package com.policycenter.model;

public class Exclusion {
    private String publicID;
    private String patternCode;
    private String name;
    private String description;
    private String exclusionText;

    public Exclusion() {}

    public Exclusion(String publicID, String patternCode, String name, String exclusionText) {
        this.publicID = publicID;
        this.patternCode = patternCode;
        this.name = name;
        this.exclusionText = exclusionText;
        this.description = name + " Clause";
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getPatternCode() { return patternCode; }
    public void setPatternCode(String patternCode) { this.patternCode = patternCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getExclusionText() { return exclusionText; }
    public void setExclusionText(String exclusionText) { this.exclusionText = exclusionText; }
}
