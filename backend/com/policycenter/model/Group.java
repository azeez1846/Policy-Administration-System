package com.policycenter.model;

public class Group {
    private String publicID;
    private String groupName;
    private String groupType; // Branch, Agency, Underwriting
    private String supervisorID;

    public Group() {}

    public Group(String publicID, String groupName, String groupType, String supervisorID) {
        this.publicID = publicID;
        this.groupName = groupName;
        this.groupType = groupType;
        this.supervisorID = supervisorID;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getGroupType() { return groupType; }
    public void setGroupType(String groupType) { this.groupType = groupType; }

    public String getSupervisorID() { return supervisorID; }
    public void setSupervisorID(String supervisorID) { this.supervisorID = supervisorID; }
}
