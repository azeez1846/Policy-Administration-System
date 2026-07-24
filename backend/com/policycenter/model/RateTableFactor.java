package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_ratetablefactor")
public class RateTableFactor {

    @Id
    @Column(name = "factor_id")
    private String factorID;

    @Column(name = "line_code")
    private String lineCode;

    @Column(name = "table_code")
    private String tableCode;

    @Column(name = "param_key")
    private String paramKey;

    @Column(name = "param_value")
    private String paramValue;

    @Column(name = "factor_value")
    private double factorValue;

    public RateTableFactor() {}

    public RateTableFactor(String factorID, String lineCode, String tableCode, String paramKey, String paramValue, double factorValue) {
        this.factorID = factorID;
        this.lineCode = lineCode;
        this.tableCode = tableCode;
        this.paramKey = paramKey;
        this.paramValue = paramValue;
        this.factorValue = factorValue;
    }

    public String getFactorID() { return factorID; }
    public void setFactorID(String factorID) { this.factorID = factorID; }

    public String getLineCode() { return lineCode; }
    public void setLineCode(String lineCode) { this.lineCode = lineCode; }

    public String getTableCode() { return tableCode; }
    public void setTableCode(String tableCode) { this.tableCode = tableCode; }

    public String getParamKey() { return paramKey; }
    public void setParamKey(String paramKey) { this.paramKey = paramKey; }

    public String getParamValue() { return paramValue; }
    public void setParamValue(String paramValue) { this.paramValue = paramValue; }

    public double getFactorValue() { return factorValue; }
    public void setFactorValue(double factorValue) { this.factorValue = factorValue; }
}
