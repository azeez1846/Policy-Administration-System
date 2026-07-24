package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_coverage")
public class Coverage {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "pattern_code")
    private String patternCode;

    @Column(name = "pattern_name")
    private String patternName;

    @Column(name = "direct_value")
    private double directValue;

    @Column(name = "choice_value")
    private String choiceValue;

    @Column(name = "calculated_term_amount")
    private double calculatedTermAmount;

    @Column(name = "currency")
    private String currency;

    public Coverage() {
        this.currency = "USD";
    }

    public Coverage(String publicID, String patternCode, String patternName, double directValue, double calculatedTermAmount) {
        this.publicID = publicID;
        this.patternCode = patternCode;
        this.patternName = patternName;
        this.directValue = directValue;
        this.calculatedTermAmount = calculatedTermAmount;
        this.currency = "USD";
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getPatternCode() { return patternCode; }
    public void setPatternCode(String patternCode) { this.patternCode = patternCode; }

    public String getPatternName() { return patternName; }
    public void setPatternName(String patternName) { this.patternName = patternName; }

    public String getName() { return patternName; }
    public void setName(String name) { this.patternName = name; }

    public double getDirectValue() { return directValue; }
    public void setDirectValue(double directValue) { this.directValue = directValue; }

    public double getLimit() { return directValue; }
    public void setLimit(double limit) { this.directValue = limit; }

    public String getChoiceValue() { return choiceValue; }
    public void setChoiceValue(String choiceValue) { this.choiceValue = choiceValue; }

    public double getDeductible() { return 1000.0; }

    public double getCalculatedTermAmount() { return calculatedTermAmount; }
    public void setCalculatedTermAmount(double calculatedTermAmount) { this.calculatedTermAmount = calculatedTermAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
