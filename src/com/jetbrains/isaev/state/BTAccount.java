package com.jetbrains.isaev.state;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Xottab
 * Date: 25.07.2014
 */
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class BTAccount implements Serializable {
    @NotNull
    private String domainName;
    @NotNull
    private String login;
    @NotNull
    private String password;
    @NotNull
    private BTAccountType type;

    // @JsonManagedReference(value = "projects")
    private List<BTProject> projects;
    private int accountID;

    public BTAccount(int accountID, String domainName, String login, String password, BTAccountType type) {
        this.accountID = accountID;
        this.domainName = domainName;
        this.login = login;
        this.password = password;
        this.type = type;
    }

    public BTAccount(@NotNull String domainName, @NotNull String login, @NotNull String password, @NotNull BTAccountType type) {

        this.domainName = domainName;
        this.login = login;
        this.password = password;
        this.type = type;
    }

    @NotNull
    public BTAccountType getType() {
        return type;
    }

    public void setType(@NotNull BTAccountType type) {
        this.type = type;
    }

    @NotNull
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(@NotNull String domainName) {
        this.domainName = domainName;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull String login) {
        this.login = login;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public List<BTProject> getProjects() {
        if (projects == null) {
            projects = new ArrayList<>(0);
        }
        return projects;
    }

    public void setProjects(List<BTProject> projects) {
        this.projects = projects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BTAccount btAccount = (BTAccount) o;

        return domainName.equals(btAccount.domainName) && login.equals(btAccount.login) && password.equals(btAccount.password) && type == btAccount.type;

    }

    @Override
    public int hashCode() {
        int result = domainName.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }
}
