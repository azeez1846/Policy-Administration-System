package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_rateroutine")
public class RateRoutine {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "routine_code")
    private String routineCode;

    @Column(name = "routine_name")
    private String routineName;

    @Column(name = "policy_line")
    private String policyLine;

    @Column(name = "description")
    private String description;

    @Column(name = "formula_expression")
    private String formulaExpression;

    public RateRoutine() {}

    public RateRoutine(String publicID, String routineCode, String routineName, String policyLine, String description, String formulaExpression) {
        this.publicID = publicID;
        this.routineCode = routineCode;
        this.routineName = routineName;
        this.policyLine = policyLine;
        this.description = description;
        this.formulaExpression = formulaExpression;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getRoutineCode() { return routineCode; }
    public void setRoutineCode(String routineCode) { this.routineCode = routineCode; }

    public String getRoutineName() { return routineName; }
    public void setRoutineName(String routineName) { this.routineName = routineName; }

    public String getPolicyLine() { return policyLine; }
    public void setPolicyLine(String policyLine) { this.policyLine = policyLine; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFormulaExpression() { return formulaExpression; }
    public void setFormulaExpression(String formulaExpression) { this.formulaExpression = formulaExpression; }
}
