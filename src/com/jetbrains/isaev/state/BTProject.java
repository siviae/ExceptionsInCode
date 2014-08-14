package com.jetbrains.isaev.state;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilya.Isaev on 30.07.2014.
 */
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class BTProject implements Serializable {

    private String fullName;
    private String shortName;

    //@JsonBackReference(value = "projects")
    private BTAccount btAccount;

    //@JsonManagedReference(value = "issues")
    private List<BTIssue> issues = new ArrayList<>();
    private Timestamp lastUpdated;
    private boolean mustBeUpdated = false;
    private int projectID;

    public BTProject(String projectFullName, String projectShortName) {
        this.fullName = projectFullName;
        this.shortName = projectShortName;
    }

    public BTProject(int projectID, String shortName, String longName) {
        this.projectID = projectID;
        this.fullName = longName;
        this.shortName = shortName;
    }

    public BTProject(String name) {
        this.fullName = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<BTIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<BTIssue> issues) {
        this.issues = issues;
    }

    public Timestamp getLastUpdated() {
        if (lastUpdated == null) lastUpdated = new Timestamp(0);
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isMustBeUpdated() {
        return mustBeUpdated;
    }

    public void setMustBeUpdated(boolean mustBeUpdated) {
        this.mustBeUpdated = mustBeUpdated;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public BTAccount getBtAccount() {

        return btAccount;
    }

    public void setBtAccount(BTAccount btAccount) {
        this.btAccount = btAccount;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getProjectID() {
        return projectID;
    }
}
