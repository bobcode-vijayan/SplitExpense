package com.bobcode.splitexpense.models;

/**
 * Created by vijayananjalees on 4/17/15.
 */
public class AccountSummaryModel {
    public String accountName;
    public String members;
    public String description;
    public String createdOn;
    public int noOfExpenses;
    public float totalSpend;
    public String status;

    public String currency;

    public AccountSummaryModel(String accountName,
                               String members,
                               String description,
                               String createdOn,
                               int noOfExpenses,
                               float totalSpend,
                               String status,
                               String currency
    ) {

        this.accountName = accountName;
        this.members = members;
        this.description = description;
        this.createdOn = createdOn;
        this.noOfExpenses = noOfExpenses;
        this.totalSpend = totalSpend;
        this.status = status;
        this.currency = currency;

    }
}
