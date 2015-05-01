package com.bobcode.splitexpense.models;

/**
 * Created by vijayananjalees on 4/21/15.
 */
public class EventSummaryModel {
    public String eventID;
    public String eventDate;
    public String description;
    public String category;
    public String whoPaid;
    public String amount;
    public String forWhom;
    public String createdOn;

    public String currency;

    public EventSummaryModel(String eventID, String eventDate,
                             String description,
                             String category,
                             String whoPaid,
                             String amount,
                             String forWhom,
                             String createdOn, String currency) {
        this.eventID = eventID;
        this.eventDate = eventDate;
        this.description = description;
        this.category = category;
        this.whoPaid = whoPaid;
        this.amount = amount;
        this.forWhom = forWhom;
        this.createdOn = createdOn;
        this.currency = currency;
    }
}
