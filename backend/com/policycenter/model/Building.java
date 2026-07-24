package com.policycenter.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pc_building")
public class Building extends EffDatedEntity {

    @Column(name = "building_num")
    private int buildingNum;

    @Column(name = "description")
    private String description;

    @Column(name = "construction_type")
    private String constructionType;

    @Column(name = "building_limit")
    private double buildingLimit;

    @Column(name = "contents_limit")
    private double contentsLimit;

    @Column(name = "year_built")
    private int yearBuilt;

    @Column(name = "num_stories")
    private int numStories;

    @Column(name = "sprinklered")
    private boolean sprinklered;

    @Column(name = "alarm_type")
    private String alarmType;

    @Column(name = "fire_protection_class")
    private String fireProtectionClass;

    @Transient
    private List<Coverage> coverages = new ArrayList<>();

    public Building() {
        super();
    }

    public Building(String publicID, int buildingNum, String description, String constructionType, double buildingLimit, double contentsLimit) {
        super();
        if (publicID != null) {
            setFixedID(publicID);
        }
        this.buildingNum = buildingNum;
        this.description = description;
        this.constructionType = constructionType;
        this.buildingLimit = buildingLimit;
        this.contentsLimit = contentsLimit;
        this.yearBuilt = 2005;
        this.numStories = 2;
        this.sprinklered = true;
        this.alarmType = "Central Station";
        this.fireProtectionClass = "Class 3";
    }

    public Building(String publicID, int buildingNum, String description, String constructionType, int yearBuilt, double buildingLimit, double contentsLimit) {
        super();
        if (publicID != null) {
            setFixedID(publicID);
        }
        this.buildingNum = buildingNum;
        this.description = description;
        this.constructionType = constructionType;
        this.yearBuilt = yearBuilt;
        this.buildingLimit = buildingLimit;
        this.contentsLimit = contentsLimit;
        this.numStories = 2;
        this.sprinklered = true;
        this.alarmType = "Central Station";
        this.fireProtectionClass = "Class 3";
    }

    public String getPublicID() { return getFixedID(); }
    public void setPublicID(String publicID) { setFixedID(publicID); }

    public int getBuildingNum() { return buildingNum; }
    public void setBuildingNum(int buildingNum) { this.buildingNum = buildingNum; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getConstructionType() { return constructionType; }
    public void setConstructionType(String constructionType) { this.constructionType = constructionType; }

    public double getBuildingLimit() { return buildingLimit; }
    public void setBuildingLimit(double buildingLimit) { this.buildingLimit = buildingLimit; }

    public double getContentsLimit() { return contentsLimit; }
    public void setContentsLimit(double contentsLimit) { this.contentsLimit = contentsLimit; }

    public int getYearBuilt() { return yearBuilt; }
    public void setYearBuilt(int yearBuilt) { this.yearBuilt = yearBuilt; }

    public int getNumStories() { return numStories; }
    public void setNumStories(int numStories) { this.numStories = numStories; }

    public int getNumberOfStories() { return numStories; }
    public void setNumberOfStories(int numStories) { this.numStories = numStories; }

    public boolean isSprinklered() { return sprinklered; }
    public void setSprinklered(boolean sprinklered) { this.sprinklered = sprinklered; }

    public String getAlarmType() { return alarmType; }
    public void setAlarmType(String alarmType) { this.alarmType = alarmType; }

    public String getFireProtectionClass() { return fireProtectionClass; }
    public void setFireProtectionClass(String fireProtectionClass) { this.fireProtectionClass = fireProtectionClass; }

    public List<Coverage> getCoverages() { return coverages; }
    public void setCoverages(List<Coverage> coverages) { this.coverages = coverages; }
    public void addCoverage(Coverage coverage) { this.coverages.add(coverage); }
}
