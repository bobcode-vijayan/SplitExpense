package com.bobcode.splitexpense.models;

/**
 * Created by vijayananjalees on 4/22/15.
 */
public class MemberDetailModel {
    public String name;
    public String displayName;
    public String comments;

    public MemberDetailModel(String name, String displayName, String comments) {
        this.name = name;
        this.displayName = displayName;
        this.comments = comments;
    }
}
