package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_glclasscode")
public class GLClassCode {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "code")
    private String code;

    @Column(name = "sub_line")
    private String subLine;

    @Column(name = "description")
    private String description;

    @Column(name = "basis_type")
    private String basisType;

    public GLClassCode() {}

    public GLClassCode(String publicID, String code, String subLine, String description, String basisType) {
        this.publicID = publicID;
        this.code = code;
        this.subLine = subLine;
        this.description = description;
        this.basisType = basisType;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getSubLine() { return subLine; }
    public void setSubLine(String subLine) { this.subLine = subLine; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBasisType() { return basisType; }
    public void setBasisType(String basisType) { this.basisType = basisType; }
}
