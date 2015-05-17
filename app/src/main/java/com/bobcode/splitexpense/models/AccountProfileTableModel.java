package com.bobcode.splitexpense.models;

/**
 * Created by vijayananjalees on 5/5/15.
 */
public class AccountProfileTableModel {

    private String accountName;
    private String description;
    private String currency;
    private String status;
    private String members;
    private String createdOn;
    private String lastModified;

    public AccountProfileTableModel(String accountName,
                                    String description,
                                    String currency,
                                    String status,
                                    String members,
                                    String createdOn,
                                    String lastModified) {
        this.accountName = accountName;
        this.description = description;
        this.currency = currency;
        this.status = status;
        this.members = members;
        this.createdOn = createdOn;
        this.lastModified = lastModified;
    }

    public AccountProfileTableModel(String accountName,
                                    String description,
                                    String currency,
                                    String status,
                                    String members,
                                    String lastModified) {
        this.accountName = accountName;
        this.description = description;
        this.currency = currency;
        this.status = status;
        this.members = members;
        this.lastModified = lastModified;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}
